package com.pythongong.aop.autoproxy;

import java.util.List;

import com.pythongong.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.pythongong.beans.aware.BeanFactoryAware;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.factory.BeanFactory;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.exception.BeansException;

public class AspectJAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    private List<AspectJExpressionPointcutAdvisor> advisors;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }
}
