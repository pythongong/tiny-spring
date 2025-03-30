package com.pythongong.aop.proxy;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.aspectj.MethodMatcher;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.aop.interceptor.MethodMatcherInterceptor;
import com.pythongong.test.aop.valid.AopTestTarget;

class ByteBuddyAopProxyTest {

    private final static MethodMatcher DEFAUL_METHOD_MATCHER = (method) -> {
        return true;
    };

    @Test
    void shouldThrowExceptionWhenAdvisedSupportIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ByteBuddyAopProxy(null);
        });
    }

    @Test
    void shouldCreateProxyForClassWithDefaultConstructor() {
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
        ByteBuddyAopProxy proxy = new ByteBuddyAopProxy(advisedSupport);
        Object proxyTarget = proxy.getProxy();

        assertNotNull(proxyTarget);
        assertTrue(proxyTarget instanceof AopTestTarget);
        assertTrue(((AopTestTarget) proxyTarget).getProxy());
    }

    @Test
    void shouldThrowExceptionWhenProxyingClassWithoutDefaultConstructor() {
        NoDefaultConstructorService service = new NoDefaultConstructorService("aa");
        MethodInterceptor interceptor1 = (invocation) -> {
            return invocation.proceed();
        };

        List<MethodMatcherInterceptor> interceptors = List.of(
                new MethodMatcherInterceptor(interceptor1, DEFAUL_METHOD_MATCHER));
        AdvisedSupport advisedSupport = new AdvisedSupport(service, interceptors);
        ByteBuddyAopProxy proxy = new ByteBuddyAopProxy(advisedSupport);

        assertThrows(Exception.class, () -> {
            proxy.getProxy();
        });
    }

    // Test service class without default constructor
    public static class NoDefaultConstructorService {
        private final String value;

        public NoDefaultConstructorService(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}