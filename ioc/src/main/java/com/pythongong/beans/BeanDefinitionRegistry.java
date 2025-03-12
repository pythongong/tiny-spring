package com.pythongong.beans;

import com.pythongong.beans.config.BeanDefinition;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String name, BeanDefinition beanDefinition);
} 
