package com.pythongong.context.annotation;

import java.lang.annotation.Annotation;

import com.pythongong.stereotype.ComponentScan;
import com.pythongong.stereotype.Value;

public enum AnnotationTypeEnum {
    
    COMPONENT_SCAN(ComponentScan.class), VALUE(Value.class);

    private final Class<? extends Annotation> annotationClass;
    
    AnnotationTypeEnum(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    // Method to get the enum constant from annotation class
    public static AnnotationTypeEnum fromAnnotation(Class<? extends Annotation> annotationClass) {
        for (AnnotationTypeEnum type : values()) {
            if (type.annotationClass.equals(annotationClass)) {
                return type;
            }
        }
        return null;
    }
        
}
