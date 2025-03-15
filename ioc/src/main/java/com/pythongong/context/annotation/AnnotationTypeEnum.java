package com.pythongong.context.annotation;

import java.lang.annotation.Annotation;

import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Bean;
import com.pythongong.stereotype.Value;

public enum AnnotationTypeEnum {
    
    VALUE(Value.class)
    , AUTO_WIRED(AutoWired.class), BEAN(Bean.class);

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
