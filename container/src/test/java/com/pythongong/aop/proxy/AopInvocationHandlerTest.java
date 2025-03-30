package com.pythongong.aop.proxy;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.interceptor.MethodMatchInterceptor;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.exception.AopConfigException;
import com.pythongong.test.aop.valid.AopTestTarget;

class AopInvocationHandlerTest {

    private final static AopTestTarget target = new AopTestTarget();
    private static Method method;
    private final static Integer[] ARGS = { 2, 3 };
    private final static String NEW_RET_VAL = "intercepted";

    @BeforeAll
    static void setUp() throws NoSuchMethodException {
        method = AopTestTarget.class.getMethod("add", int.class, int.class);
    }

    @Test
    void shouldThrowExceptionWhenAdvisedSupportIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new AopInvocationHandler(null);
        });
    }

    @Test
    void shouldInvokeMethodDirectlyWhenNoInterceptors() throws Throwable {
        MethodInterceptor interceptor1 = (invocation) -> {
            return invocation.proceed();
        };

        MethodInterceptor interceptor2 = (invocation) -> {
            return invocation.proceed();
        };

        List<MethodInterceptor> interceptors = List.of(interceptor1, interceptor2);
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);
        AopInvocationHandler handler = new AopInvocationHandler(advisedSupport);
        assertEquals(5, handler.invoke(target, method, ARGS));
    }

    @Test
    void shouldNotInvokeMethodThroughDynamicInterceptors() throws Throwable {
        MethodInterceptor interceptor1 = (invocation) -> {
            return NEW_RET_VAL;
        };

        MethodMatchInterceptor dynamicMatchMethodInterceptor = new MethodMatchInterceptor(interceptor1,
                (method) -> {
                    return false;
                });

        MethodInterceptor interceptor2 = (invocation) -> {
            return invocation.proceed();
        };

        List<MethodInterceptor> interceptors = List.of(dynamicMatchMethodInterceptor, interceptor2);
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);
        AopInvocationHandler handler = new AopInvocationHandler(advisedSupport);
        assertEquals(5, handler.invoke(target, method, ARGS));
    }

    @Test
    void shouldInvokeMethodThroughDynamicInterceptors() throws Throwable {
        MethodInterceptor interceptor1 = (invocation) -> {
            return NEW_RET_VAL;
        };

        MethodMatchInterceptor dynamicMatchMethodInterceptor = new MethodMatchInterceptor(interceptor1,
                (method) -> {
                    return true;
                });

        MethodInterceptor interceptor2 = (invocation) -> {
            return invocation.proceed();
        };

        List<MethodInterceptor> interceptors = List.of(dynamicMatchMethodInterceptor, interceptor2);
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);
        AopInvocationHandler handler = new AopInvocationHandler(advisedSupport);
        assertThrows(AopConfigException.class, () -> handler.invoke(target, method, ARGS));
    }

}