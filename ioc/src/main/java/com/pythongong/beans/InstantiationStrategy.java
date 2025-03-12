package com.pythongong.beans;

import java.lang.reflect.Constructor;

import com.pythongong.exception.IocException;

public interface InstantiationStrategy {
    Object instance(Class<?> clazz, Constructor<?> constructor, Object[] args) throws IocException;
    
}
