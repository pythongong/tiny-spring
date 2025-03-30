package com.pythongong.util;

import java.lang.reflect.Method;

import com.pythongong.aop.advice.JoinPoint;
import com.pythongong.aop.advice.ProceedingJoinPoint;

import lombok.Builder;

@Builder
public record AdviceMethodParam(Method advicMethod, JoinPoint joinPoint, ProceedingJoinPoint proceedingJoinPoint,
        Object retVal, Object aspect) {

}
