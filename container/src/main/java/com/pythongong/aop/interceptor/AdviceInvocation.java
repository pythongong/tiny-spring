package com.pythongong.aop.interceptor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.pythongong.aop.JoinPoint;
import com.pythongong.exception.AopConfigException;
import com.pythongong.util.CheckUtils;

import lombok.Builder;

@Builder
public record AdviceInvocation(Object target, Method method, JoinPoint joinPoint,
        List<MethodInterceptor> methodInterceptors, AtomicInteger interceptedNum) {

    public AdviceInvocation {
        String name = AdviceInvocation.class.getSimpleName();
        CheckUtils.nullArgs(target, name, "target");
        CheckUtils.nullArgs(method, name, "method");
        CheckUtils.nullArgs(joinPoint, name, "jointPoint");
        CheckUtils.emptyCollection(methodInterceptors, name, "methodInterceptors");

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
            method.setAccessible(true);
            return method.invoke(target, joinPoint.args());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AopConfigException("method invoke in failure");
        }

    }

}
