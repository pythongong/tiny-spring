package com.pythongong.beans.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import com.pythongong.exception.BeansException;
import com.pythongong.util.CheckUtils;

public class SimpleInstantiation implements InstantiationStrategy {

    @Override
    public Object instance(Class<?> clazz, Constructor<?> constructor, Object[] args) throws BeansException {
        CheckUtils.nullArgs(clazz, "SimpleInstantiation.instance recevies null bean class");
        try {
            if (constructor == null) {
                return clazz.getDeclaredConstructor().newInstance();
            }
            return constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new BeansException(String.format("Failed to instantiate class {%s}", clazz.getName()), e);
        }

    }

   

    
}