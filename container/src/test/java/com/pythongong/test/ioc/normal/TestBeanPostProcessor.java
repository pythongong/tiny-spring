package com.pythongong.test.ioc.normal;

import com.pythongong.beans.config.BeforeInitializationProcessor;
import com.pythongong.stereotype.Component;

import lombok.Data;

@Data
@Component("testBeanPostProcessor")
public class TestBeanPostProcessor implements BeforeInitializationProcessor {

    @Override
    public void postProcessBeforeInitialization(Object bean, String beanName) {
        if (bean instanceof TestComponent) {
            ((TestComponent) bean).setPostProcessed(true);
        }

    }

}