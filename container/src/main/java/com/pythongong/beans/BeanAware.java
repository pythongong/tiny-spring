package com.pythongong.beans;

import com.pythongong.exception.BeansException;

public interface BeanAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
