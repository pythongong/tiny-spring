package com.pythongong.core.filter;

import java.lang.annotation.Annotation;

import com.pythongong.util.ClassUtils;

public class AnnotationTypeFilter implements TypeFilter {

    private final Class<? extends Annotation> annotationType;

    public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public boolean match(Class<?> target) {
        Object object = ClassUtils.findAnnotation(target, annotationType);
        return object != null;
    }
    
}
