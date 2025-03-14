package com.pythongong.beans.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.pythongong.exception.BeansException;

public class SimpleInstantiation implements InstantiationStrategy {

    @Override
    public Object instance(Class<?> clazz, Constructor<?> constructor, Object[] args) throws BeansException {
        Object bean;
        try {
            if (constructor == null) {
                bean = clazz.getDeclaredConstructor().newInstance();
            } else {
                bean = constructor.newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new BeansException("Failed to instantiate [" + clazz.getName() + "]", e);
        }
        return bean;

    }

   

    
}