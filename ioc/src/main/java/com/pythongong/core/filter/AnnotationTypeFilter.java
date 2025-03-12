package com.pythongong.core.filter;

import java.lang.annotation.Annotation;

public class AnnotationTypeFilter implements TypeFilter {

    private final Class<? extends Annotation> annotationType;

    public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public boolean match(Class<?> target) {
        Object object = target.getAnnotation(annotationType);
        return object != null;
    }
    
}
