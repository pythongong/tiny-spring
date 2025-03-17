package com.pythongong.beans;

import com.pythongong.exception.BeansException;

public interface BeanFactory {

    Object getBean(String beanName) throws BeansException;

    <T> T getBean(String beanName, Class<T> requiredType) throws BeansException;
} 