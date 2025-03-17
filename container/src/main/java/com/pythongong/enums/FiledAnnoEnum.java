package com.pythongong.enums;

import java.lang.annotation.Annotation;

import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Value;

public enum FiledAnnoEnum {
    
    VALUE(Value.class), AUTO_WIRED(AutoWired.class);

    private final Class<? extends Annotation> annotationClass;
    
    FiledAnnoEnum(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    

    // Method to get the enum constant from annotation class
    public static FiledAnnoEnum fromAnnotation(Class<? extends Annotation> annotationClass) {
        for (FiledAnnoEnum type : values()) {
            if (type.annotationClass.equals(annotationClass)) {
                return type;
            }
        }
        return null;
    }
        
}
