package com.pythongong.aop;

import java.util.function.Supplier;

import com.pythongong.exception.AopException;

@FunctionalInterface
public interface MethodInterceptor {

    Object invoke(Supplier<Object> proceed) throws AopException;

}
