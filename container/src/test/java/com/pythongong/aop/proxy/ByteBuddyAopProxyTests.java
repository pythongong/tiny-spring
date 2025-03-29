package com.pythongong.aop.proxy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.MethodInterceptor;
import com.pythongong.test.aop.valid.AopTestInterface;
import com.pythongong.test.aop.valid.AopTestTarget;

public class ByteBuddyAopProxyTests {

    static final String CORRECT_EXPRESS = "execution(* com.pythongong.test.aop.AopTestInterface.*(..))";

    static final String WRONG_EXPRESS = "execution(* com.pythongong.test.aop.DuplicateAopTestInterface.*(..))";

    @Test
    @DisplayName("Should intercept method when matcher matches")
    void shouldInterceptMethodWhenMatcherMatches() {
        // Arrange
        AopTestInterface target = new AopTestTarget();

        MethodInterceptor interceptor = invocation -> {
            Object object = invocation.proceed();
            if (object instanceof Boolean) {
                Boolean isProxy = (Boolean) object;
                isProxy = true;
                return isProxy;
            }
            return object;
        };

        AdvisedSupport advisedSupport = AdvisedSupport.builder()
                .target(target)
                .methodInterceptor(interceptor)

                .build();

        // Act
        assertFalse(target.getProxy());
        ByteBuddyAopProxy proxy = new ByteBuddyAopProxy(advisedSupport);
        AopTestInterface proxyObject = (AopTestInterface) proxy.getProxy();

        // Assert
        assertNotNull(proxyObject);
        assertTrue(proxyObject.getProxy());

    }

    @Test
    @DisplayName("Should throw exception when target class is invalid")
    void shouldThrowExceptionWhenTargetClassIsInvalid() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> new ByteBuddyAopProxy(null));
    }

    @Test
    @DisplayName("Should not intercept method when matcher doesn't match")
    void shouldNotInterceptMethodWhenMatcherDoesntMatch() {
        // Arrange
        AopTestInterface target = new AopTestTarget();

        MethodInterceptor interceptor = invocation -> {
            Object object = invocation.proceed();
            if (object instanceof Boolean) {
                Boolean isProxy = (Boolean) object;
                isProxy = true;
                return isProxy;
            }
            return object;
        };
        AdvisedSupport advisedSupport = AdvisedSupport.builder()
                .target(target)
                .methodInterceptor(interceptor)

                .build();

        // Act
        assertFalse(target.getProxy());
        ByteBuddyAopProxy proxy = new ByteBuddyAopProxy(advisedSupport);
        AopTestInterface proxyObject = (AopTestInterface) proxy.getProxy();

        // Assert
        assertNotNull(proxyObject);
        assertFalse(proxyObject.getProxy());
    }

    @Test
    @DisplayName("Should handle method with parameters correctly")
    void shouldHandleMethodWithParametersCorrectly() {
        // Arrange
        AopTestInterface target = new AopTestTarget();

        MethodInterceptor interceptor = invocation -> {
            Object object = invocation.proceed();
            if (object instanceof Integer) {
                return ((Integer) object) * 2;
            }
            return object;
        };

        AdvisedSupport advisedSupport = AdvisedSupport.builder()
                .target(target)
                .methodInterceptor(interceptor)

                .build();

        // Act
        ByteBuddyAopProxy proxy = new ByteBuddyAopProxy(advisedSupport);
        AopTestInterface proxyObject = (AopTestInterface) proxy.getProxy();
        int result = proxyObject.add(3, 4);

        // Assert
        assertEquals(14, result);
    }

}
