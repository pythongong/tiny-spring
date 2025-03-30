package com.pythongong.aop.proxy;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.exception.AopConfigException;
import com.pythongong.test.aop.valid.AopTestInterface;
import com.pythongong.test.aop.valid.AopTestTarget;
import com.pythongong.test.ioc.normal.TestBean;

class JdkDynamicAopProxyTest {

    @Test
    void shouldThrowExceptionWhenAdvisedSupportIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new JdkDynamicAopProxy(null);
        });
    }

    @Test
    void shouldCreateProxyForInterfaceImplementation() {
        AopTestTarget target = new AopTestTarget();
        MethodInterceptor interceptor1 = (invocation) -> {
            return invocation.proceed();
        };

        MethodInterceptor interceptor2 = (invocation) -> {
            invocation.proceed();
            return true;
        };

        List<MethodInterceptor> interceptors = List.of(interceptor1, interceptor2);
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);
        JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(advisedSupport);
        Object proxyTarget = proxy.getProxy();

        assertNotNull(proxyTarget);
        assertTrue(proxyTarget instanceof AopTestInterface);
        assertTrue(((AopTestInterface) proxyTarget).getProxy());
    }

    @Test
    void shouldThrowExceptionWhenTargetDoesNotImplementInterface() {
        MethodInterceptor interceptor1 = (invocation) -> {
            return invocation.proceed();
        };

        List<MethodInterceptor> interceptors = List.of(interceptor1);
        TestBean target = new TestBean();
        AdvisedSupport advisedSupport = new AdvisedSupport(target, interceptors);

        assertThrows(AopConfigException.class, () -> {
            new JdkDynamicAopProxy(advisedSupport);
        });
    }

}