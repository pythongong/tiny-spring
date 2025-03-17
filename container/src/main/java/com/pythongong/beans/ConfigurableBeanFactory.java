package com.pythongong.beans;

import com.pythongong.beans.config.BeanPostProcessor;

public interface ConfigurableBeanFactory extends BeanFactory{
    
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
    
}
