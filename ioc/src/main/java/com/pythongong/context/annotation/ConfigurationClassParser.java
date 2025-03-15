package com.pythongong.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanReference;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.context.support.PropertyResolver;
import com.pythongong.enums.AnnotationTypeEnum;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Component;
import com.pythongong.stereotype.ComponentScan;
import com.pythongong.stereotype.PostConstruct;
import com.pythongong.stereotype.PreDestroy;
import com.pythongong.stereotype.Scope;
import com.pythongong.stereotype.Value;

public class ConfigurationClassParser {

    private final PackageClassScanner scanner;

    private final PropertyResolver propertyResolver;

    private final DefaultListableBeanFactory beanFactory;

    public ConfigurationClassParser(PropertyResolver propertyResolver, DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.scanner = new PackageClassScanner();
        this.propertyResolver = propertyResolver;
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

        return doParse(componentScan, declaredClass);
    }

    private Set<BeanDefinition> doParse(ComponentScan componentScan, Class<?> declaredClass){
        String[] basePackages = componentScan.basePackages();
        if (basePackages.length == 0) {
            basePackages = new String[1];
            basePackages[0] = declaredClass.getPackageName();
        }
        Set<Class<?>> beanClasses = scanner.scan(basePackages);
        return new HashSet<>(beanClasses.stream().map(this::createBeanDefinition).toList());
    }

    private BeanDefinition createBeanDefinition(Class<?> beanClass) {
        String beanName = generateBeanName(beanClass);

        // init method:
        Method initMethod = findInitOrDestoryMethod(beanClass, PostConstruct.class);

        // destroy method:
        Method destoryMethod = findInitOrDestoryMethod(beanClass, PreDestroy.class);

        ScopeEnum scopeEnum = extractScope(beanClass);

        PropertyValueList propertyValueList = createPropertyValueList(beanClass);

        return BeanDefinition.builder()
        .beanName(beanName)
        .beanClass(beanClass)
        .propertyValueList(propertyValueList)
        .initMethod(initMethod)
        .destroyMethod(destoryMethod)
        .scope(scopeEnum)
        .build();
    }

    private ScopeEnum extractScope(Class<?> beanClass) {
        Scope scope = beanClass.getAnnotation(Scope.class);
        return scope == null ? ScopeEnum.SINGLETON : ScopeEnum.fromScope(scope.value());
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


    private PropertyValueList createPropertyValueList(Class<?> beanClass) {
        PropertyValueList propertyValueList = new PropertyValueList();
        Field[] fields = beanClass.getFields();
        for (Field field : fields) {
            PropertyValue prpertyValue = accessFieldAnnotations(field);
            if (prpertyValue != null) {
                propertyValueList.addPropertyValue(prpertyValue);
            }
        }
        return propertyValueList;
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
        String beanName = field.getClass().getName();
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
        if (beanDefinition != null) {
            beanName = beanDefinition.beanName();
        }

        BeanReference beanReference = new BeanReference(beanName);
        return new PropertyValue(field.getName(), beanReference);
    }


}
