package com.pythongong.test.ioc.normal;

import com.pythongong.beans.aware.BeanFactoryAware;
import com.pythongong.beans.aware.BeanNameAware;
import com.pythongong.beans.factory.BeanFactory;
import com.pythongong.stereotype.Component;

import lombok.Getter;

@Getter
@Component("awareBean")
public class AwareBean implements BeanNameAware, BeanFactoryAware {
    private String beanName;
    private BeanFactory beanFactory;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }
}