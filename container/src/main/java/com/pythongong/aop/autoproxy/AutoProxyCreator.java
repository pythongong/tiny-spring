package com.pythongong.aop.autoproxy;

import com.pythongong.beans.aware.BeanFactoryAware;
import com.pythongong.beans.config.BeanProcessor;
import com.pythongong.beans.factory.BeanFactory;
import com.pythongong.exception.BeansException;

public interface AutoProxyCreator extends BeanFactoryAware, BeanProcessor {

    BeanFactory getBeanFactory();

    Object create(Object bean, String beanName) throws BeansException;
}
