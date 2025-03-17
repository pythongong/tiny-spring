package com.pythongong.beans.support;

import java.lang.reflect.Method;

import com.pythongong.beans.config.DisposableBean;
import com.pythongong.util.CheckUtils;

public class DisposableBeanAdapter implements DisposableBean {

    private final Object bean;

    private final Method destroyMethod;

    public DisposableBeanAdapter(Object bean, Method destroyMethod) {
        CheckUtils.nullArgs(bean, "DisposableBeanAdapter recevies null bean");
        CheckUtils.nullArgs(destroyMethod, "DisposableBeanAdapter recevies null destroyMethod");

        this.bean = bean;
        this.destroyMethod = destroyMethod;
    }

    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        destroyMethod.invoke(bean);
    }
    
}
