package com.pythongong.beans.impl;

import com.pythongong.beans.BeanFactory;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.exception.IocException;

public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory {

    @Override
    public Object getBean(String name) throws IocException {
        return doGetBean(name, null);
    }

    

    @Override
    public Object getBean(String name, Object... args) throws IocException {
        return doGetBean(name, args);
    }

    private Object doGetBean(String beanname, Object[] args) {
        Object bean = getSingleton(beanname);
        if (bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition(beanname);
        return createBean(beanname, beanDefinition, args);
    }



    protected abstract BeanDefinition getBeanDefinition(String beanName) throws IocException;

    protected abstract Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws IocException;
    
}
