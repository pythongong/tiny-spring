package com.pythongong.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import com.pythongong.beans.FactoryBean;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanReference;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.context.support.PropertyResolver;
import com.pythongong.enums.AnnotationTypeEnum;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Bean;
import com.pythongong.stereotype.Component;
import com.pythongong.stereotype.ComponentScan;
import com.pythongong.stereotype.Configuration;
import com.pythongong.stereotype.PostConstruct;
import com.pythongong.stereotype.PreDestroy;
import com.pythongong.stereotype.Scope;
import com.pythongong.stereotype.Value;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.StringUtils;

public class ConfigurableClassParser {

    private final ConfigurableClassScanner scanner;

    private final PropertyResolver propertyResolver;

    private Set<BeanDefinition> beanDefinitions;

    public ConfigurableClassParser(PropertyResolver propertyResolver) {
        this.scanner = new ConfigurableClassScanner();
        this.propertyResolver = propertyResolver;
        beanDefinitions = new HashSet<>();
    }
    
    public Set<BeanDefinition> parse(Class<?> declaredClass) {
        Annotation[] annotations = declaredClass.getAnnotations();

        if (annotations == null) {
            return Collections.emptySet();
        }
        ComponentScan componentScan = declaredClass.getAnnotation(ComponentScan.class);
        if (componentScan == null) {
            throw new IllegalArgumentException();
        }
        String[] basePackages = componentScan.basePackages();
        Set<Class<?>> beanClasses = scanner.scan(ClassUtils.isArrayEmpty(basePackages) 
        ? new String[]{declaredClass.getPackageName()} : basePackages);

        return doParse(beanClasses);
    }

    private Set<BeanDefinition> doParse(Set<Class<?>> beanClasses){
        this.beanDefinitions = new HashSet<>();
        beanClasses.forEach(this::createBeanDefinition);
        this.beanDefinitions.forEach(this::fillPropertyValueList);
        return this.beanDefinitions;
    }

    private void createBeanDefinition(Class<?> beanClass) {

        String beanName = generateBeanName(beanClass);

        // init method:
        Method initMethod = findInitOrDestoryMethod(beanClass, PostConstruct.class);

        // destroy method:
        Method destoryMethod = findInitOrDestoryMethod(beanClass, PreDestroy.class);
        
        ScopeEnum scopeEnum = extractScope(beanClass);

        Constructor<?> constructor = getConfigurableConstrucor(beanClass);
        BeanDefinition beanDefinition = BeanDefinition.builder()
        .constructor(constructor)
        .beanName(beanName)
        .beanClass(beanClass)
        .initMethod(initMethod)
        .destroyMethod(destoryMethod)
        .scope(scopeEnum)
        .build();

        addBeanDef(beanDefinition);

        if (beanClass.isAnnotationPresent(Configuration.class)) {
            createFactoryBeanDefinitions(beanClass);
        }
    }

    private void createFactoryBeanDefinitions(Class<?> beanClass) {
        Method[] methods = beanClass.getMethods();
        Arrays.stream(methods)
        .filter(method -> method.isAnnotationPresent(Bean.class))
        .forEach(method -> {
            Bean beanAnno = method.getAnnotation(Bean.class);
            String beanName = beanAnno.value();
            if (StringUtils.isEmpty(beanName)) {
                beanName = method.getReturnType().getName();
            }
            FactoryBean<Object> factoryBean = () -> method;
            ScopeEnum scope = extractScope(method.getClass());
            BeanDefinition beanDefinition = BeanDefinition.builder()
            .beanName(beanName)
            .beanClass(factoryBean.getClass())
            .scope(scope)
            .build();
            addBeanDef(beanDefinition);
        });;
        
    }

    private void addBeanDef(BeanDefinition beanDefinition) {
        if (!this.beanDefinitions.add(beanDefinition)) {
            throw new BeansException("conflict bean");
        }
    }

    private ScopeEnum extractScope(Class<?> beanClass) {
        Scope scope = ClassUtils.findAnnotation(beanClass, Scope.class);
        return scope == null ? ScopeEnum.SINGLETON : scope.value();
    }

    private Constructor<?>  getConfigurableConstrucor(Class<?> beanClass) {
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        List<Constructor<?>> configurabeConsturctors = Arrays.stream(declaredConstructors)
        .filter(constructor -> constructor.getAnnotationsByType(AutoWired.class) != null).toList();

        if (configurabeConsturctors.isEmpty()) {
            return null;
        }
        
        if (configurabeConsturctors.size() > 1) {
            throw new BeansException("conflict constructors");
        }
        return configurabeConsturctors.get(0);
    }

    private String generateBeanName(Class<?> beanClass) {
        Component component = beanClass.getAnnotation(Component.class);
        if (component == null || component.value().isBlank()) {
            return beanClass.getName();
        }
        return component.value();
    }

    private Method findInitOrDestoryMethod(Class<?> beanClass, Class<? extends Annotation> anotationClass) {
        List<Method> methods = Arrays.stream(beanClass.getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(anotationClass)).map(method -> {
            if (method.getParameterCount() != 0) {
                throw new BeansException (
                    String.format("Method '%s' with @%s must not have argument: %s", 
                    method.getName(), anotationClass.getSimpleName(), beanClass.getName()));
            }
            return method;
        }).toList();

        if (methods.isEmpty()) {
            return null;
        }

        if (methods.size() > 1) {
            throw new BeansException(String.format("Multiple methods with @%s found in class: %s"
            , anotationClass.getSimpleName(), beanClass.getName()));
        }

        return methods.get(0);
    }


    private void fillPropertyValueList(BeanDefinition beandDefinition) {
        Class<?> beanClass = beandDefinition.beanClass();
        PropertyValueList propertyValueList = beandDefinition.propertyValueList();
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            PropertyValue prpertyValue = accessFieldAnnotations(field);
            if (prpertyValue == null) {
                continue;
            }
            propertyValueList.addPropertyValue(prpertyValue);
        }
    }

    private PropertyValue accessFieldAnnotations(Field field) {
        Annotation[] annotations = field.getAnnotations();
        for (Annotation annotation : annotations) {
            AnnotationTypeEnum annotationTypeEnum = AnnotationTypeEnum.fromAnnotation(annotation.annotationType());
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

    private PropertyValue getValuedField(Value value, Field field) {
        String property = propertyResolver.getProperty(value.value());
        return new PropertyValue(field.getName(), property);
    }

    private PropertyValue getAutowiredField(AutoWired autoWired, Field field) {
        String beanName = autoWired.name();
        if (!StringUtils.isEmpty(beanName)) {
            return new PropertyValue(field.getName(), new BeanReference(beanName));
        }

        Class<?> fieldClass = field.getType();
        BeanDefinition beanDefinition = getBeanDefinitionByType(fieldClass);
        beanName = beanDefinition.beanName();
        return new PropertyValue(field.getName(), new BeanReference(beanName));
    }

    private BeanDefinition getBeanDefinitionByType(Class<?> requiredType) {
        List<BeanDefinition> requiredDefs = beanDefinitions.stream().filter(beanDefinition -> beanDefinition.beanClass().equals(requiredType)).toList();

        if (requiredDefs.isEmpty()) {
            throw new NoSuchElementException();
        }

        if (requiredDefs.size() > 1) {
            throw new BeansException("complict bean");
        }

        return requiredDefs.get(0);
    }


}
