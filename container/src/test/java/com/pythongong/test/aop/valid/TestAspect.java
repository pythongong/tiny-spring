package com.pythongong.test.aop.valid;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.pythongong.aop.advice.JoinPoint;
import com.pythongong.aop.advice.ProceedingJoinPoint;

@Aspect
public class TestAspect {

    @Before("execution(* com.example.*.*(..))")
    public void beforeAdvice(JoinPoint joinPoint) {
    }

    @After("execution(* com.example.*.*(..))")
    public void afterAdvice(JoinPoint joinPoint) {
    }

    @Around("execution(* com.example.*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    @AfterReturning("execution(* com.example.*.*(..))")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
    }
}