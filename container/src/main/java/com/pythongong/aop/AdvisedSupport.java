package com.pythongong.aop;

import com.pythongong.util.CheckUtils;

public record AdvisedSupport(TargetSource targetSource, MethodInterceptor methodInterceptor, MethodMatcher matcher) {

    public AdvisedSupport {
        CheckUtils.nullArgs(targetSource, "AdvisedSupport recevies null targetSource");
        CheckUtils.nullArgs(methodInterceptor, "AdvisedSupport recevies null methodInterceptor");
        CheckUtils.nullArgs(matcher, "AdvisedSupport recevies null matcher");
    }
}