package com.pythongong.beans.support;

import java.lang.reflect.Method;

import com.pythongong.beans.config.DisposableBean;
import com.pythongong.exception.BeansException;

public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;

    private final Method destroyMethod;

    public DisposableBeanAdapter(Object bean, Method destroyMethod) {
        this.bean = bean;
        this.destroyMethod = destroyMethod;
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        if (destroyMethod == null) {
            throw new BeansException("Could not find an destroy method named: " + destroyMethod);
        }
        destroyMethod.invoke(bean);
    }
    
}
