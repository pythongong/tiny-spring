package com.pythongong.beans.support;

import java.lang.reflect.Method;

import com.pythongong.DisposableBean;
import com.pythongong.exception.BeansException;

public class DisposableBeanAdapter implements DisposableBean{

    private final Object bean;

    private final String destroyMethodName;

    public DisposableBeanAdapter(Object bean, String destroyMethodName) {
        this.bean = bean;
        this.destroyMethodName = destroyMethodName;
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }

        Method destroyMethod = bean.getClass().getMethod(destroyMethodName);
        if (destroyMethod == null) {
            throw new BeansException("Could not find an destroy method named: " + destroyMethodName);
        }
        destroyMethod.invoke(destroyMethod);
    }
    
}
