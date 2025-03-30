package com.pythongong.test.aop.valid;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.pythongong.aop.JoinPoint;
import com.pythongong.aop.ProceedingJoinPoint;
import com.pythongong.stereotype.Component;

@Aspect
@Component("testAspectTwo")
public class TestAspectTwo {

    @AfterReturning("execution(* com.pythongong.test.aop.valid.*.*(..))")
    public void afterReturningAdvice(JoinPoint joinPoint, Object result) {
        AdviceOrder.execute("afterReturning2: " + joinPoint.methodName());
    }

    @Around("execution(* com.pythongong.test.aop.valid.*.*(..))")
    public Object aroundAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        AdviceOrder.execute("around2: " + joinPoint.getJoinPoint().methodName());
        return joinPoint.proceed();
    }

    @Before("execution(* com.pythongong.test.aop.valid.*.*(..))")
    public void beforeAdvice(JoinPoint joinPoint) {
        AdviceOrder.execute("before2: " + joinPoint.methodName());
    }

    @After("execution(* com.pythongong.test.aop.valid.*.*(..))")
    public void afterAdvice(JoinPoint joinPoint) {
        AdviceOrder.execute("after2: " + joinPoint.methodName());
    }

}
