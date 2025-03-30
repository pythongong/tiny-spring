package com.pythongong.aop.proxy;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.aspectj.MethodMatcher;
import com.pythongong.aop.interceptor.MethodMatcherInterceptor;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.exception.AopConfigException;
import com.pythongong.test.aop.valid.AopTestTarget;

class AopInvocationHandlerTest {

    private static final AopTestTarget target = new AopTestTarget();
    private static Method method;
    private static final Integer[] ARGS = { 2, 3 };
    private static final String NEW_RET_VAL = "intercepted";

    private static final MethodMatcher DEFAUL_METHOD_MATCHER = (method) -> {
        return true;
    };

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

        List<MethodMatcherInterceptor> interceptors = List.of(
                new MethodMatcherInterceptor(interceptor1, DEFAUL_METHOD_MATCHER),
                new MethodMatcherInterceptor(interceptor2, DEFAUL_METHOD_MATCHER));
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);
        AopInvocationHandler handler = new AopInvocationHandler(advisedSupport);
        assertEquals(5, handler.invoke(target, method, ARGS));
    }

    @Test
    void shouldNotInvokeMethodThroughDynamicInterceptors() throws Throwable {
        MethodInterceptor interceptor1 = (invocation) -> {
            return NEW_RET_VAL;
        };

        MethodInterceptor interceptor2 = (invocation) -> {
            return invocation.proceed();
        };

        List<MethodMatcherInterceptor> interceptors = List.of(
                new MethodMatcherInterceptor(interceptor1, (method) -> {
                    return false;
                }),
                new MethodMatcherInterceptor(interceptor2, DEFAUL_METHOD_MATCHER));
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);
        AopInvocationHandler handler = new AopInvocationHandler(advisedSupport);
        assertEquals(5, handler.invoke(target, method, ARGS));
    }

    @Test
    void shouldInvokeMethodThroughDynamicInterceptors() throws Throwable {
        MethodInterceptor interceptor1 = (invocation) -> {
            return NEW_RET_VAL;
        };

        MethodInterceptor interceptor2 = (invocation) -> {
            return invocation.proceed();
        };

        List<MethodMatcherInterceptor> interceptors = List.of(
                new MethodMatcherInterceptor(interceptor1, DEFAUL_METHOD_MATCHER),
                new MethodMatcherInterceptor(interceptor2, DEFAUL_METHOD_MATCHER));
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);
        AopInvocationHandler handler = new AopInvocationHandler(advisedSupport);
        assertThrows(AopConfigException.class, () -> handler.invoke(target, method, ARGS));
    }

}