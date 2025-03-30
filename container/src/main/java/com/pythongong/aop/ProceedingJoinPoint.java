package com.pythongong.aop;

import com.pythongong.aop.interceptor.AdviceInvocation;

public class ProceedingJoinPoint {

    private final AdviceInvocation invocation;

    public ProceedingJoinPoint(AdviceInvocation invocation) {
        this.invocation = invocation;
    }

    public Object proceed() {
        return invocation.proceed();
    }

    public JoinPoint getJoinPoint() {
        return invocation.joinPoint();
    }

}
