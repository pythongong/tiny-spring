package com.pythongong.aop.interceptor;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.aop.JoinPoint;
import com.pythongong.test.aop.valid.AopTestTarget;

@ExtendWith(MockitoExtension.class)
class AdviceInvocationTest {

    private final static Integer[] ARGS = { 2, 3 };

    private static AopTestTarget testTarget;
    private static Method method;
    private static JoinPoint joinPoint;

    @BeforeAll
    static void setUp() throws Exception {
        testTarget = new AopTestTarget();
        method = AopTestTarget.class.getMethod("add", int.class, int.class);
        joinPoint = new JoinPoint(method.getName(), method.getParameterTypes(), (Object[]) ARGS);
    }

    @Test
    void testConstructorWithNullTarget() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdviceInvocation.builder()
                    .target(null)
                    .method(method)
                    .build();
        });
    }

    @Test
    void testConstructorWithNullMethod() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdviceInvocation.builder()
                    .target(testTarget)
                    .method(null)
                    .build();
        });
    }

    @Test
    void testProceedWithNoInterceptors() {
        assertThrows(IllegalArgumentException.class, () -> {
            AdviceInvocation.builder()
                    .target(testTarget)
                    .method(method)
                    .joinPoint(joinPoint)
                    .methodInterceptors(Collections.emptyList())
                    .build();
        });

    }

    @Test
    void testProceedWithInterceptors() {
        String retVal = "intercepted1";
        MethodInterceptor interceptor1 = (invocation) -> {
            return retVal;
        };

        MethodInterceptor interceptor2 = (invocation) -> {
            return invocation.proceed();
        };
        List<MethodInterceptor> interceptors = List.of(interceptor1, interceptor2);
        AdviceInvocation invocation = AdviceInvocation.builder()
                .target(new AopTestTarget())
                .method(method)
                .joinPoint(joinPoint)
                .methodInterceptors(interceptors)
                .build();

        assertEquals(retVal, invocation.proceed());
        assertFalse(interceptors.size() == invocation.interceptedNum().get());
    }

    @Test
    void testProceedWithValidMethod() {
        MethodInterceptor interceptor1 = (invocation) -> {
            return invocation.proceed();
        };

        MethodInterceptor interceptor2 = (invocation) -> {
            return invocation.proceed();
        };

        List<MethodInterceptor> interceptors = List.of(interceptor1, interceptor2);
        AdviceInvocation invocation = AdviceInvocation.builder()
                .target(new AopTestTarget())
                .method(method)
                .joinPoint(joinPoint)
                .methodInterceptors(interceptors)
                .build();

        assertEquals(5, invocation.proceed());
        assertTrue(interceptors.size() == invocation.interceptedNum().get());

    }

}