package com.pythongong.util;

import java.lang.annotation.Annotation;

public class ClassUtils {
    
    private ClassUtils(){}

    public static final Object NULL_OBJECT = new Object();

    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return contextClassLoader == null ? ClassUtils.class.getClassLoader() : contextClassLoader;
    }

    public static <A extends Annotation> A findAnnotation(Class<?> target, Class<A> annoClass) {
        A targetAnno = target.getAnnotation(annoClass);
        if (targetAnno != null) {
            return targetAnno;
        }
        for (Annotation anno : target.getAnnotations()) {
            Class<? extends Annotation> annoType = anno.annotationType();
            if (annoType.getPackageName().equals("java.lang.annotation")) {
                continue;
            }
            targetAnno = findAnnotation(annoType, annoClass);
            if (targetAnno != null) {
                break;
            }
        }
        return targetAnno;
    }

    public static boolean isArrayEmpty(Object[] array) {
        return array == null || array.length == 0;
    }
}
