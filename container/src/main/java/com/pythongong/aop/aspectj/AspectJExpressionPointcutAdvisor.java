package com.pythongong.aop.aspectj;

import java.lang.reflect.Method;

import com.pythongong.enums.AdviceEnum;
import com.pythongong.util.CheckUtils;

import lombok.Builder;

@Builder
public record AspectJExpressionPointcutAdvisor(String aspectName, AdviceEnum adviceEnum, Method method,
        AspectJExpressionPointcut pointcut) {

    public AspectJExpressionPointcutAdvisor {
        CheckUtils.nullArgs(method, "AspectJExpressionPointcutAdvisor", "method");
        CheckUtils.nullArgs(pointcut, "AspectJExpressionPointcutAdvisor", "pointcut");
    }
}
