package com.pythongong.util;

import java.lang.reflect.Method;

import com.pythongong.aop.JoinPoint;
import com.pythongong.aop.ProceedingJoinPoint;

import lombok.Builder;

@Builder
public record AdviceMethodParam(Method advicMethod, JoinPoint joinPoint, ProceedingJoinPoint proceedingJoinPoint,
        Object retVal, Object aspect) {

    public AdviceMethodParam {
        String methodName = "AdviceMethodParam";
        CheckUtils.nullArgs(advicMethod, methodName, "advicMethod");
        CheckUtils.nullArgs(aspect, methodName, "aspect");
    }
}
