package com.pythongong.aop.interceptor;

import java.lang.reflect.Method;

import com.pythongong.exception.AopConfigException;
import com.pythongong.util.AdviceMethodParam;
import com.pythongong.util.AopUtils;

public class AfterReturningMethodInterceptor implements MethodInterceptor {
    private final Method afterReturing;

    private final Object aspect;

    public AfterReturningMethodInterceptor(Method afterReturning, Object aspect) {
        this.afterReturing = afterReturning;
        this.aspect = aspect;
    }

    @Override
    public Object invoke(AdviceInvocation invocation) throws AopConfigException {
        Object retVal = invocation.proceed();
        return AopUtils.invokeAdvice(AdviceMethodParam.builder()
                .aspect(aspect)
                .advicMethod(afterReturing)
                .retVal(retVal)
                .build());
    }
}
