package com.pythongong.beans;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.context.event.ApplicationEventMulticaster;
import com.pythongong.exception.BeansException;

public interface ConfigurableListableBeanFactory extends SingletonBeanRegistry, ConfigurableBeanFactory, ListableBeanFactory{


    void preInstantiateSingletons() throws BeansException;

    ApplicationEventMulticaster initApplicationEventMulticaster();

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    
}
