package com.pythongong.beans.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.pythongong.beans.InstantiationStrategy;
import com.pythongong.exception.IocException;

public class SimpleInstantiation implements InstantiationStrategy {

    @Override
    public Object instance(Class<?> clazz, Constructor<?> constructor, Object[] args) throws IocException {
        Object bean;
        try {
            if (constructor == null) {
                bean = clazz.getDeclaredConstructor().newInstance();
            } else {
                bean = constructor.newInstance(args);
            }
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new IocException("Failed to instantiate [" + clazz.getName() + "]", e);
        }
        return bean;

    }

   

    
}