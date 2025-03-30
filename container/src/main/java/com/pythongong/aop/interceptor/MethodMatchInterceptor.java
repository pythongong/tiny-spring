package com.pythongong.aop.interceptor;

import java.lang.reflect.Method;

import com.pythongong.aop.aspectj.MethodMatcher;
import com.pythongong.exception.AopConfigException;

public record MethodMatchInterceptor(MethodInterceptor methodInterceptor, MethodMatcher methodMatcher)
        implements MethodMatcher, MethodInterceptor {

    @Override
    public Object invoke(AdviceInvocation invocation) throws AopConfigException {
        return methodInterceptor.invoke(invocation);
    }

    @Override
    public boolean matches(Method method) {
        return methodMatcher.matches(method);
    }

}
