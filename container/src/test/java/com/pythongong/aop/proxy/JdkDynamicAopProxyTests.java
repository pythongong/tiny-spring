package com.pythongong.aop.proxy;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.MethodInterceptor;
import com.pythongong.aop.MethodMatcher;
import com.pythongong.aop.TargetSource;
import com.pythongong.aop.aspectj.AspectJExpressionPointcut;
import com.pythongong.exception.AopException;
import com.pythongong.test.aop.AopTestInterface;
import com.pythongong.test.aop.AopTestTarget;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class JdkDynamicAopProxyTests {

    static final String correctExpress = "execution(* com.pythongong.test.aop.AopTestInterface.*(..))";

    static final String wrongExpress = "execution(* com.pythongong.test.aop.AopTestTarget.*(..))";

    @Test
    @DisplayName("Should intercept method when matcher matches")
    void shouldInterceptMethodWhenMatcherMatches() {
        // Arrange
        AopTestInterface target = new AopTestTarget();
        TargetSource targetSource = new TargetSource(target);
        MethodInterceptor interceptor = proceed -> {
            Object object = proceed.get();
            if (object instanceof Boolean) {
                Boolean isProxy = (Boolean) object;
                isProxy = true;
                return isProxy;
            }
            return object;
        };
        MethodMatcher matcher = new AspectJExpressionPointcut(correctExpress);
        AdvisedSupport advisedSupport = new AdvisedSupport(targetSource, interceptor, matcher);

        // Act
        assertFalse(target.getProxy());
        JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(advisedSupport);
        AopTestInterface proxyObject = (AopTestInterface) proxy.getProxy();

        // Assert
        assertNotNull(proxyObject);
        assertTrue(proxyObject.getProxy());

    }

    @Test
    @DisplayName("Should throw exception when target class is invalid")
    void shouldThrowExceptionWhenTargetClassIsInvalid() {
        // Arrange
        Object invalidTarget = new Object();
        TargetSource targetSource = new TargetSource(invalidTarget);
        MethodInterceptor interceptor = proceed -> {
            Object object = proceed.get();
            if (object instanceof Boolean) {
                Boolean isProxy = (Boolean) object;
                isProxy = true;
                return isProxy;
            }
            return object;
        };
        MethodMatcher matcher = new AspectJExpressionPointcut(correctExpress);
        AdvisedSupport advisedSupport = new AdvisedSupport(targetSource, interceptor,
                matcher);

        // Act & Assert
        assertThrows(AopException.class, () -> new JdkDynamicAopProxy(advisedSupport));
    }

    @Test
    @DisplayName("Should not intercept method when matcher doesn't match")
    void shouldNotInterceptMethodWhenMatcherDoesntMatch() {
        // Arrange
        AopTestInterface target = new AopTestTarget();
        TargetSource targetSource = new TargetSource(target);
        MethodInterceptor interceptor = proceed -> {
            Object object = proceed.get();
            if (object instanceof Boolean) {
                Boolean isProxy = (Boolean) object;
                isProxy = true;
                return isProxy;
            }
            return object;
        };
        MethodMatcher matcher = new AspectJExpressionPointcut(wrongExpress);
        AdvisedSupport advisedSupport = new AdvisedSupport(targetSource, interceptor,
                matcher);

        // Act
        JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(advisedSupport);
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
        TargetSource targetSource = new TargetSource(target);
        MethodInterceptor interceptor = proceed -> {
            Object object = proceed.get();
            if (object instanceof Integer) {
                return ((Integer) object) * 2;
            }
            return object;
        };
        MethodMatcher matcher = new AspectJExpressionPointcut(correctExpress);
        AdvisedSupport advisedSupport = new AdvisedSupport(targetSource, interceptor,
                matcher);

        // Act
        JdkDynamicAopProxy proxy = new JdkDynamicAopProxy(advisedSupport);
        AopTestInterface proxyObject = (AopTestInterface) proxy.getProxy();
        int result = proxyObject.add(3, 4);

        // Assert
        assertEquals(14, result);
    }
}