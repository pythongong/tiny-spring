package com.pythongong.test.ioc.normal;

import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.stereotype.Component;

import lombok.Data;

@Data
@Component("testBeanPostProcessor")
public class TestBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof TestComponent) {
            ((TestComponent) bean).setPostProcessed(true);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        return bean;
    }
}