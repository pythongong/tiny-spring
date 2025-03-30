package com.pythongong.aop.advice;

public record JoinPoint(String methodName, Class<?>[] parameterTypes, Object[] args) {
}
