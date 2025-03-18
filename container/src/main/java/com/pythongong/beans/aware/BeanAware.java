package com.pythongong.beans.aware;

import com.pythongong.beans.factory.BeanFactory;
import com.pythongong.exception.BeansException;

@FunctionalInterface
public interface BeanAware extends Aware {

    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
