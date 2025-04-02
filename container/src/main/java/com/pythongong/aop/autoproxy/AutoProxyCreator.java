package com.pythongong.aop.autoproxy;

import com.pythongong.beans.factory.BeanFactory;
import com.pythongong.exception.BeansException;

public interface AutoProxyCreator {

    BeanFactory getBeanFactory();

    void setBeanFactory(BeanFactory beanFactory);

    Object create(Object bean, String beanName) throws BeansException;
}
