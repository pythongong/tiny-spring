package com.pythongong.aop.interceptor;

import java.lang.reflect.Method;
import com.pythongong.exception.AopConfigException;
import com.pythongong.util.AdviceMethodParam;
import com.pythongong.util.AopUtils;

public class BeforeMethodInterceptor implements MethodInterceptor {

    private final Method before;

    private final Object aspect;

    public BeforeMethodInterceptor(Method before, Object aspect) {
        this.before = before;
        this.aspect = aspect;
    }

    @Override
    public Object invoke(AdviceInvocation invocation) throws AopConfigException {
        AopUtils.invokeAdvice(AdviceMethodParam.builder()
                .advicMethod(before)
                .aspect(aspect)
                .joinPoint(invocation.joinPoint())
                .build());
        return invocation.proceed();
    }

}
