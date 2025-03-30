package com.pythongong.aop.proxy;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.aspectj.MethodMatcher;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.aop.interceptor.MethodMatcherInterceptor;
import com.pythongong.test.aop.valid.AopTestInterface;
import com.pythongong.test.aop.valid.AopTestTarget;
import com.pythongong.test.ioc.normal.TestBean;

class ProxyFactoryTest {

    private static final MethodMatcher DEFAUL_METHOD_MATCHER = (method) -> {
        return true;
    };

    @Test
    void shouldCreateJdkProxyForInterface() {

        AopTestTarget target = new AopTestTarget();
        MethodInterceptor interceptor1 = (invocation) -> {
            return invocation.proceed();
        };

        MethodInterceptor interceptor2 = (invocation) -> {
            invocation.proceed();
            return true;
        };

        List<MethodMatcherInterceptor> interceptors = List.of(
                new MethodMatcherInterceptor(interceptor1, DEFAUL_METHOD_MATCHER),
                new MethodMatcherInterceptor(interceptor2, DEFAUL_METHOD_MATCHER));
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);
        Object proxyTarget = ProxyFactory.createProxy(advisedSupport);

        assertNotNull(proxyTarget);
        assertTrue(proxyTarget instanceof AopTestInterface);
        assertTrue(((AopTestInterface) proxyTarget).getProxy());
    }

    @Test
    void shouldCreateByteBuddyProxyForClass() {
        TestBean target = new TestBean();
        MethodInterceptor interceptor1 = (invocation) -> {
            return invocation.proceed();
        };

        List<MethodMatcherInterceptor> interceptors = List.of(
                new MethodMatcherInterceptor(interceptor1, DEFAUL_METHOD_MATCHER));
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);
        Object proxyTarget = ProxyFactory.createProxy(advisedSupport);

        assertNotNull(proxyTarget);
        assertTrue(proxyTarget instanceof TestBean);
    }

    @Test
    void shouldThrowExceptionWhenAdvisedSupportIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            ProxyFactory.createProxy(null);
        });
    }

}