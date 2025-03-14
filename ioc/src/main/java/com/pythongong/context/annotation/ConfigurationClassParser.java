package com.pythongong.context.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanReference;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.context.support.PropertyResolver;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.ComponentScan;
import com.pythongong.stereotype.Value;

public class ConfigurationClassParser {

    private final ClassPathBeanDefinitionScanner scanner;

    private final PropertyResolver propertyResolver;

    private final DefaultListableBeanFactory beanFactory;

    public ConfigurationClassParser(PropertyResolver propertyResolver, DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
        this.scanner = new ClassPathBeanDefinitionScanner((BeanDefinitionRegistry) beanFactory);
        this.propertyResolver = propertyResolver;
    }
    
    public Set<BeanDefinition> parse(Class<?> declaredClass) {
        Set<BeanDefinition> beanDefinitions = new HashSet<>();

        Annotation[] annotations = declaredClass.getAnnotations();

        if (annotations == null) {
            return Collections.emptySet();
        }

        ComponentScan componentScan = declaredClass.getAnnotation(ComponentScan.class);

        if (componentScan == null) {
            throw new IllegalArgumentException();
        }

        beanDefinitions.addAll(parse(componentScan, declaredClass));

        return beanDefinitions;
    }

    private Set<BeanDefinition> parse(ComponentScan componentScan, Class<?> declaredClass){
        String[] basePackages = componentScan.basePackages();
        if (basePackages.length == 0) {
            basePackages = new String[1];
            basePackages[0] = declaredClass.getPackageName();
        }
        Set<BeanDefinition> beanDefinitions = scanner.scan(basePackages);
        beanDefinitions.forEach(this::getProperties);
        return beanDefinitions;
    }

    public void getProperties(BeanDefinition beanDefinition) {
        Class<?> benClass = beanDefinition.beanClass();
        PropertyValueList propertyValueList = beanDefinition.propertyValueList(); 

        Field[] fields = benClass.getFields();

        for (Field field : fields) {
            PropertyValue prpertyValue = accessFieldAnnotations(field);
            if (prpertyValue != null) {
                propertyValueList.addPropertyValue(prpertyValue);
            }
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
        String defaultBeanName = field.getClass().getName();
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition(defaultBeanName);
        if (beanDefinition == null) {
            throw new BeansException("Can't find bean definition for: " + defaultBeanName);
        }

        BeanReference beanReference = new BeanReference(defaultBeanName);
        return new PropertyValue(field.getName(), beanReference);
    }


}
