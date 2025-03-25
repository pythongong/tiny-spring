package com.pythongong.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.TargetSource;
import com.pythongong.exception.AopException;
import com.pythongong.util.ClassUtils;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {

    private final AdvisedSupport advisedSupport;

    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        this.advisedSupport = advisedSupport;
        TargetSource targetSource = advisedSupport.targetSource();
        Class<?>[] targetClasses = targetSource.getTargetClass();
        if (ClassUtils.isArrayEmpty(targetClasses)) {
            throw new AopException(
                    String.format("Target type {%s} is wrong", targetSource.getClass().getName()));
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = advisedSupport.targetSource().target();
        if (!advisedSupport.matcher().matches(method)) {
            return method.invoke(target, args);
        }
        return advisedSupport.methodInterceptor().invoke(() -> {
            try {
                return method.invoke(target, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AopException("Fail to invoke method ");
            }
        });
    }

    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),
                advisedSupport.targetSource().getTargetClass(),
                this);
    }

}
