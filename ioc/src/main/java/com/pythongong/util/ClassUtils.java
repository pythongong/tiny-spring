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

    public static Method findInitOrDestoryMethod(Class<?> beanClass, Class<? extends Annotation> anotationClass) {
        List<Method> methods = Arrays.stream(beanClass.getDeclaredMethods())
        .filter(method -> method.isAnnotationPresent(anotationClass)).map(method -> {
            if (method.getParameterCount() != 0) {
                throw new BeansException (
                    String.format("Method '%s' with @%s must not have argument: %s", 
                    method.getName(), anotationClass.getSimpleName(), beanClass.getName()));
            }
            return method;
        }).toList();

        if (methods.isEmpty()) {
            return null;
        }

        if (methods.size() > 1) {
            throw new BeansException(String.format("Multiple methods with @%s found in class: %s"
            , anotationClass.getSimpleName(), beanClass.getName()));
        }

        return methods.get(0);
    }
}
