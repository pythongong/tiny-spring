package com.pythongong.aop.interceptor;

import java.lang.reflect.Method;

import com.pythongong.exception.AopConfigException;
import com.pythongong.util.AdviceMethodParam;
import com.pythongong.util.AopUtils;

public class AfterMethodInterceptor implements MethodInterceptor {

    private final Method after;

    private final Object aspect;

    public AfterMethodInterceptor(Method after, Object aspect) {
        this.after = after;
        this.aspect = aspect;
    }

    @Override
    public Object invoke(AdviceInvocation invocation) throws AopConfigException {
        Object result;
        try {
            result = invocation.proceed();
        } finally {
            AopUtils.invokeAdvice(AdviceMethodParam.builder()
                    .advicMethod(after)
                    .aspect(aspect)
                    .joinPoint(invocation.joinPoint())
                    .build());
        }

        return result;
    }

}
