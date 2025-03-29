package com.pythongong.test.aop.invalid;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

@Aspect
public class InvalidBeforeAdvice {
    @Before("execution(* com.example.*.*(..))")
    public void invalidBeforeAdvice(String invalidParam) {
    }
}
