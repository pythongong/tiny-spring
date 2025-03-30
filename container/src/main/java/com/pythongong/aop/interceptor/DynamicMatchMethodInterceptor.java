package com.pythongong.aop.interceptor;

import com.pythongong.aop.aspectj.MethodMatcher;
import com.pythongong.exception.AopConfigException;

public record DynamicMatchMethodInterceptor(MethodInterceptor methodInterceptor, MethodMatcher methodMatcher)
        implements MethodInterceptor {

    @Override
    public Object invoke(AdviceInvocation invocation) throws AopConfigException {
        return methodInterceptor.invoke(invocation);
    }

}
