package com.pythongong.beans;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.exception.BeansException;

public interface ConfigurableListableBeanFactory extends ListableBeanFactory, SingletonBeanRegistry, ConfigurableBeanFactory{

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons() throws BeansException;
    
}
