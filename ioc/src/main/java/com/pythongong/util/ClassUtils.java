package com.pythongong.util;

public class ClassUtils {
    
    private ClassUtils(){}

    public static final Object NULL_OBJECT = new Object();

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return contextClassLoader == null ? ClassUtils.class.getClassLoader() : contextClassLoader;
    }
}
