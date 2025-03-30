package com.pythongong.aop.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.pythongong.aop.advice.JoinPoint;
import com.pythongong.exception.AopConfigException;
import com.pythongong.util.CheckUtils;

import lombok.Builder;

@Builder
public record AdviceInvocation(Object target, Method method, JoinPoint joinPoint,
        List<MethodInterceptor> methodInterceptors, AtomicInteger interceptedNum) {

    public AdviceInvocation {
        CheckUtils.nullArgs(target, "MethodInvocation recevies null target");
        CheckUtils.nullArgs(method, "MethodInvocation recevies null method");
        interceptedNum = new AtomicInteger(0);
    }

    public Object proceed() {
        if (interceptedNum.get() == (methodInterceptors.size())) {
            return invokeRealMethod();
        }
        int interceptorIndex = interceptedNum.getAndIncrement();
        return methodInterceptors.get(interceptorIndex).invoke(this);
    }

    private Object invokeRealMethod() {
        try {
            return method.invoke(target, joinPoint.args());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AopConfigException("method invoke in failure");
        }

    }

}
