package com.pythongong.beans.support;

import java.lang.reflect.Constructor;

import com.pythongong.exception.BeansException;

public interface InstantiationStrategy {
    
    Object instance(Class<?> clazz, Constructor<?> constructor, Object[] args) throws BeansException;
    
}
