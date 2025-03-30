package com.pythongong.aop;

public record JoinPoint(String methodName, Class<?>[] parameterTypes, Object[] args) {
}
