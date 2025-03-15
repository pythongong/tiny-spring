package com.pythongong.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import com.pythongong.exception.BeansException;

public class ClassUtils {
    
    private ClassUtils(){}

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if (classLoader == null) {
            classLoader = ClassUtils.class.getClassLoader();
        }
        return classLoader;
    }
}
