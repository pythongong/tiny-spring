package com.pythongong.context.annotation;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.stereotype.ComponentScan;

public class ConfigurationClassParser {

    private final ClassPathBeanDefinitionScanner scanner;

    public ConfigurationClassParser(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.scanner = new ClassPathBeanDefinitionScanner(beanDefinitionRegistry);
    }
    
    public Set<BeanDefinition> parse(Class<?> declaredClass) {
        Set<BeanDefinition> beanDefinitions = new HashSet<>();

        Annotation[] annotations = declaredClass.getAnnotations();

        if (annotations == null) {
            return Collections.emptySet();
        }

        for (Annotation annotation : annotations) {
            AnnotationTypeEnum annotationEnum = AnnotationTypeEnum.fromAnnotation(annotation.annotationType());
            if (annotationEnum == null) {
                continue;
            }
            switch (annotationEnum) {
                case COMPONENT_SCAN:
                    beanDefinitions.addAll(parse((ComponentScan) annotation, declaredClass));
                    break;
                default:
                    break;
            }
        }

        return beanDefinitions;
    }

    public Set<BeanDefinition> parse(ComponentScan componentScan, Class<?> declaredClass){
        String[] basePackages = componentScan.basePackages();
        if (basePackages.length == 0) {
            basePackages = new String[1];
            basePackages[0] = declaredClass.getPackageName();
        }
        return scanner.scan(basePackages);
    }


}
