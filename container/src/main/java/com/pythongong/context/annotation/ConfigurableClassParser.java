/*
 * Copyright 2025 Cheng Gong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pythongong.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

import org.aspectj.lang.annotation.Aspect;

import com.pythongong.beans.config.*;
import com.pythongong.context.impl.PropertyResolver;
import com.pythongong.enums.FiledAnnoEnum;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.exception.BeansException;
import com.pythongong.exception.DuplicateBeanException;
import com.pythongong.stereotype.*;
import com.pythongong.util.AopUtils;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.StringUtils;

/**
 * Parser that processes configuration classes and component scanning to create
 * bean definitions.
 * <p>
 * This class handles configuration classes marked with {@link Configuration}
 * and processes
 * component scanning through {@link ComponentScan}. It creates bean definitions
 * from both
 * annotated classes and {@link Bean} methods in configuration classes.
 * 
 * <p>
 * Key responsibilities:
 * <ul>
 * <li>Component scanning and bean definition creation</li>
 * <li>Processing of {@link Configuration} classes</li>
 * <li>Handling of dependency injection annotations</li>
 * <li>Processing of bean lifecycle annotations</li>
 * </ul>
 *
 * @author Cheng Gong
 * @see Configuration
 * @see ComponentScan
 * @see Bean
 * @see BeanDefinition
 */
public class ConfigurableClassParser {

    /** Scanner used to find candidate component classes */
    private final ConfigurableClassScanner scanner;

    /** Resolver for property placeholders */
    private final PropertyResolver propertyResolver;

    /** Set of bean definitions discovered during parsing */
    private Set<BeanDefinition> beanDefinitions;

    private BeanDefinition aopBeanPostProcessorDefinition;

    /**
     * Creates a new parser with the specified property resolver.
     *
     * @param propertyResolver the resolver for property placeholders
     * @throws IllegalArgumentException if propertyResolver is null
     */
    public ConfigurableClassParser(PropertyResolver propertyResolver) {
        CheckUtils.nullArgs(propertyResolver, "ConfigurableClassParser receives null propertyResolver");
        this.scanner = new ConfigurableClassScanner();
        this.propertyResolver = propertyResolver;
    }

    /**
     * Parses a configuration class to discover and create bean definitions.
     * <p>
     * Processes {@link ComponentScan} configuration and scans for components
     * in the specified base packages.
     *
     * @param declaredClass the configuration class to parse
     * @return a Set of discovered bean definitions
     * @throws IllegalArgumentException if declaredClass is null
     */
    public Set<BeanDefinition> parse(Class<?> declaredClass) {
        CheckUtils.nullArgs(declaredClass, "ConfigurableClassParser.parse receives null class");
        Annotation[] annotations = declaredClass.getAnnotations();
        if (ClassUtils.isArrayEmpty(annotations)) {
            return Collections.emptySet();
        }
        ComponentScan componentScan = ClassUtils.findAnnotation(declaredClass, ComponentScan.class);
        if (componentScan == null) {
            return Collections.emptySet();
        }

        String[] basePackages = componentScan.basePackages();
        Set<Class<?>> beanClasses = scanner.scan(
                ClassUtils.isArrayEmpty(basePackages) ? new String[] { declaredClass.getPackageName() } : basePackages);
        this.beanDefinitions = new HashSet<>(beanClasses.size());
        beanClasses.forEach(beanClass -> createBeanDefinition(beanClass));
        this.beanDefinitions.forEach(this::fillfieldValueList);
        return this.beanDefinitions;
    }

    /**
     * Creates a bean definition from a component class.
     * <p>
     * Processes component configuration including scope, lifecycle methods,
     * and factory methods for {@link Configuration} classes.
     *
     * @param beanClass the class to create a bean definition for
     * @throws DuplicateBeanException if a bean definition already exists
     */
    private void createBeanDefinition(Class<?> beanClass) {
        String beanName = generateBeanName(beanClass);
        BeanDefinition beanDefinition = BeanDefinition.builder()
                .constructor(getSuitableConstrucor(beanClass))
                .beanName(beanName)
                .beanClass(beanClass)
                .initMethodName(findInitOrDestoryMethod(beanClass, PostConstruct.class))
                .destroyMethodName(findInitOrDestoryMethod(beanClass, PreDestroy.class))
                .scope(extractScope(beanClass))
                .build();

        addBeanDef(beanDefinition);

        if (beanClass.isAnnotationPresent(Configuration.class)) {
            createFactoryBeanDefinitions(beanClass, beanName);
        }

        if (beanClass.isAnnotationPresent(Aspect.class)) {
            if (aopBeanPostProcessorDefinition == null) {
                aopBeanPostProcessorDefinition = AopUtils.definiteAopBeanPostProcessor();
                addBeanDef(aopBeanPostProcessorDefinition);
            }
            AopUtils.addAdvisors(beanDefinition, aopBeanPostProcessorDefinition);
        }
    }

    /**
     * Creates bean definitions for methods marked with {@link Bean} in
     * configuration classes.
     *
     * @param beanClass the configuration class to process
     * @param beanName2
     */
    private void createFactoryBeanDefinitions(Class<?> beanClass, String factoryName) {
        Method[] methods = beanClass.getMethods();
        Arrays.stream(methods)
                .filter(method -> method.isAnnotationPresent(Bean.class))
                .forEach(method -> {
                    Bean beanAnno = method.getAnnotation(Bean.class);
                    Class<?> returnType = method.getReturnType();
                    if (returnType == null) {
                        throw new BeansException("Bean method must have a return type: " + method.getName());
                    }
                    String beanName = beanAnno.value();

                    FactoryDefinition factpryDefinition = new FactoryDefinition(factoryName, method.getName(),
                            method.getParameterTypes());

                    BeanDefinition beanDefinition = BeanDefinition.builder()
                            .beanName(StringUtils.isEmpty(beanName) ? returnType.getName() : beanName)
                            .beanClass(returnType)
                            .initMethodName(beanAnno.init())
                            .destroyMethodName(beanAnno.destroy())
                            .scope(extractScope(returnType))
                            .factoryDefinition(factpryDefinition)
                            .build();

                    addBeanDef(beanDefinition);
                });
    }

    /**
     * Adds a bean definition to the set of discovered definitions.
     *
     * @param beanDefinition the bean definition to add
     * @throws DuplicateBeanException if a definition already exists
     */
    private void addBeanDef(BeanDefinition beanDefinition) {
        if (!this.beanDefinitions.add(beanDefinition)) {
            throw new DuplicateBeanException(beanDefinition.beanName(), beanDefinition.beanClass());
        }
    }

    /**
     * Extracts the scope from a bean class, defaulting to singleton.
     *
     * @param beanClass the class to check for scope
     * @return the scope enum value
     */
    private ScopeEnum extractScope(Class<?> beanClass) {
        Scope scope = beanClass.getAnnotation(Scope.class);
        return scope == null ? ScopeEnum.SINGLETON : scope.value();
    }

    /**
     * Finds an {@link AutoWired} annotated constructor.
     *
     * @param beanClass the class to check for autowired constructors
     * @return the autowired constructor or null if none found
     * @throws BeansException if multiple autowired constructors are found
     */
    private Constructor<?> getSuitableConstrucor(Class<?> beanClass) {
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        List<Constructor<?>> configurabeConsturctors = Arrays.stream(declaredConstructors)
                .filter(constructor -> constructor.isAnnotationPresent(AutoWired.class)).toList();

        if (configurabeConsturctors.isEmpty()) {
            try {
                return beanClass.getDeclaredConstructor();
            } catch (NoSuchMethodException | SecurityException e) {
                throw new BeansException(
                        String.format("{%s} doesn't have a suitable constructor", beanClass.getName()));
            }
        }

        if (configurabeConsturctors.size() > 1) {
            throw new BeansException(
                    String.format("Mulitiple constructor injections're found in {%s}", beanClass.getName()));
        }
        return configurabeConsturctors.get(0);
    }

    /**
     * Generates a bean name from a component class.
     *
     * @param beanClass the class to generate a name for
     * @return the bean name
     */
    private String generateBeanName(Class<?> beanClass) {
        Component component = beanClass.getAnnotation(Component.class);
        if (component != null && !StringUtils.isEmpty(component.value())) {
            return component.value();
        }

        Configuration configuration = beanClass.getAnnotation(Configuration.class);
        if (configuration != null && !StringUtils.isEmpty(configuration.value())) {
            return configuration.value();
        }
        return beanClass.getName();
    }

    /**
     * Finds initialization or destruction methods.
     *
     * @param beanClass      the class to check for lifecycle methods
     * @param anotationClass the lifecycle annotation to look for
     * @return the lifecycle method or null if none found
     * @throws BeansException if multiple lifecycle methods are found
     */
    private String findInitOrDestoryMethod(Class<?> beanClass, Class<? extends Annotation> anotationClass) {
        List<Method> methods = Arrays.stream(beanClass.getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(anotationClass)).map(method -> {
                    if (method.getParameterCount() != 0) {
                        throw new BeansException(
                                String.format("Method '%s' with @%s must not have argument: %s",
                                        method.getName(), anotationClass.getSimpleName(), beanClass.getName()));
                    }
                    return method;
                }).toList();

        if (methods.isEmpty()) {
            return null;
        }

        if (methods.size() > 1) {
            throw new BeansException(String.format("Multiple methods with @%s found in class: %s",
                    anotationClass.getSimpleName(), beanClass.getName()));
        }

        return methods.get(0).getName();
    }

    /**
     * Processes field annotations to create property values.
     *
     * @param beandDefinition the bean definition to process
     */
    private void fillfieldValueList(BeanDefinition beandDefinition) {
        Class<?> beanClass = beandDefinition.beanClass();
        FieldValueList fieldValueList = beandDefinition.fieldValueList();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            FieldValue prpertyValue = accessFieldAnnotations(field);
            if (prpertyValue == null) {
                continue;
            }
            fieldValueList.add(prpertyValue);
        }
    }

    /**
     * Processes annotations on a field to create property values.
     *
     * @param field the field to process
     * @return the property value or null if no relevant annotations
     */
    private FieldValue accessFieldAnnotations(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            FiledAnnoEnum annotationTypeEnum = FiledAnnoEnum.fromAnnotation(annotation.annotationType());
            switch (annotationTypeEnum) {
                case VALUE:
                    return getValuedField((Value) annotation, field);
                case AUTO_WIRED:
                    return getAutowiredField((AutoWired) annotation, field);
                default:
                    break;
            }
        }
        return null;
    }

    /**
     * Creates a property value for a {@link Value} annotated field.
     *
     * @param value the Value annotation
     * @param field the annotated field
     * @return the property value
     */
    private FieldValue getValuedField(Value value, Field field) {
        Object property = propertyResolver.getProperty(value.value(), field.getType());
        return new FieldValue(field.getName(), property);
    }

    /**
     * Creates a property value for an {@link AutoWired} annotated field.
     *
     * @param autoWired the AutoWired annotation
     * @param field     the annotated field
     * @return the property value
     */
    private FieldValue getAutowiredField(AutoWired autoWired, Field field) {
        String beanName = autoWired.value();
        if (!StringUtils.isEmpty(beanName)) {
            return new FieldValue(field.getName(), new BeanReference(beanName));
        }

        Class<?> fieldClass = field.getType();
        BeanDefinition beanDefinition = getBeanDefinitionByType(fieldClass);
        beanName = beanDefinition.beanName();
        return new FieldValue(field.getName(), new BeanReference(beanName));
    }

    /**
     * Finds a bean definition by type.
     *
     * @param requiredType the required bean type
     * @return the matching bean definition
     * @throws NoSuchElementException if no matching bean is found
     * @throws BeansException         if multiple matching beans are found
     */
    private BeanDefinition getBeanDefinitionByType(Class<?> requiredType) {
        List<BeanDefinition> requiredDefs = beanDefinitions.stream()
                .filter(beanDefinition -> beanDefinition.beanClass().equals(requiredType))
                .toList();

        if (requiredDefs.isEmpty()) {
            throw new NoSuchElementException();
        }

        if (requiredDefs.size() > 1) {
            throw new BeansException("conflicting beans found");
        }

        return requiredDefs.get(0);
    }
}