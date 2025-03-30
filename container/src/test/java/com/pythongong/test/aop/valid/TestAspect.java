package com.pythongong.test.aop.valid;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.pythongong.aop.advice.JoinPoint;
import com.pythongong.aop.advice.ProceedingJoinPoint;
import com.pythongong.stereotype.Component;

@Aspect
@Component("testAspect")
public class TestAspect {

    @Before("execution(* com.pythongong.test.aop.valid.*.*(..))")
    public void beforeAdvice(JoinPoint joinPoint) {
    }

    @After("execution(* com.pythongong.test.aop.valid.*.*(..))")
    public void afterAdvice(JoinPoint joinPoint) {
    }

    @Around("execution(* com.pythongong.test.aop.valid.*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }

    @AfterReturning("execution(* com.pythongong.test.aop.valid.*.*(..))")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
    }
}