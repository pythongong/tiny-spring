package com.pythongong.beans;

import com.pythongong.exception.BeansException;

public interface BeanFactory {

    Object getBean(String name) throws BeansException;

    <T> T getBean(String name, Class<T> requiredType) throws BeansException;
} 