package com.pythongong.aop.interceptor;

import java.lang.reflect.Method;

import com.pythongong.aop.ProceedingJoinPoint;
import com.pythongong.exception.AopConfigException;
import com.pythongong.util.AdviceMethodParam;
import com.pythongong.util.AopUtils;

public class AroundMethodInterceptor implements MethodInterceptor {

    private final Method around;

    private final Object aspect;

    public AroundMethodInterceptor(Method around, Object aspect) {
        this.around = around;
        this.aspect = aspect;
    }

    @Override
    public Object invoke(AdviceInvocation invocation) throws AopConfigException {
        return AopUtils.invokeAdvice(AdviceMethodParam.builder()
                .aspect(aspect)
                .advicMethod(around)
                .proceedingJoinPoint(new ProceedingJoinPoint(invocation))
                .build());
    }

}
