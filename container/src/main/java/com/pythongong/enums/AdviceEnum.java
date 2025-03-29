package com.pythongong.enums;

import java.lang.annotation.Annotation;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Around;

public enum AdviceEnum {
    Before(Before.class), After(After.class), AfterReturning(AfterReturning.class), Around(Around.class);

    /**
     * The actual annotation class that this enum constant represents
     */
    private final Class<? extends Annotation> annotationClass;

    private AdviceEnum(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * Gets the enum constant corresponding to the given annotation class.
     *
     * @param annotationClass the annotation class to look up
     * @return the corresponding FiledAnnoEnum constant, or null if not found
     */
    public static AdviceEnum fromAnnotation(Class<? extends Annotation> annotationClass) {
        for (AdviceEnum type : values()) {
            if (type.annotationClass.equals(annotationClass)) {
                return type;
            }
        }
        return null;
    }
}
