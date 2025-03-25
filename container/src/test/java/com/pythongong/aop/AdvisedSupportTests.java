package com.pythongong.aop;

import org.junit.jupiter.api.Test;

import com.pythongong.aop.aspectj.AspectJExpressionPointcut;
import com.pythongong.test.ioc.normal.TestBean;

import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class AdvisedSupportTests {

    @Test
    @DisplayName("Constructor should create valid instance with all parameters")
    void constructorShouldCreateValidInstance() {
        // Arrange
        TargetSource targetSource = new TargetSource(new TestBean());
        MethodInterceptor interceptor = (proceed) -> {
            return proceed.get();
        };
        MethodMatcher matcher = new AspectJExpressionPointcut("execution(* com.pythongong.test.utils.TestBean.*(..))");

        // Act
        AdvisedSupport advised = new AdvisedSupport(targetSource, interceptor, matcher);

        // Assert
        assertNotNull(advised);
        assertEquals(targetSource, advised.targetSource());
        assertEquals(interceptor, advised.methodInterceptor());
        assertEquals(matcher, advised.matcher());
    }

    @Test
    @DisplayName("Constructor should throw exception when targetSource is null")
    void constructorShouldThrowExceptionWhenTargetSourceIsNull() {
        // Arrange
        MethodInterceptor interceptor = (proceed) -> {
            return proceed.get();
        };
        MethodMatcher matcher = new AspectJExpressionPointcut("execution(* com.pythongong.test.utils.TestBean.*(..))");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AdvisedSupport(null, interceptor, matcher));
    }

    @Test
    @DisplayName("Constructor should throw exception when methodInterceptor is null")
    void constructorShouldThrowExceptionWhenMethodInterceptorIsNull() {
        // Arrange
        TargetSource targetSource = new TargetSource(new Object());
        MethodMatcher matcher = new AspectJExpressionPointcut("execution(* com.pythongong.test.utils.TestBean.*(..))");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AdvisedSupport(targetSource, null, matcher));
    }

    @Test
    @DisplayName("Constructor should throw exception when matcher is null")
    void constructorShouldThrowExceptionWhenMatcherIsNull() {
        // Arrange
        TargetSource targetSource = new TargetSource(new Object());
        MethodInterceptor interceptor = (proceed) -> {
            return proceed.get();
        };

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AdvisedSupport(targetSource, interceptor, null));
    }

    @Test
    @DisplayName("Record components should be immutable")
    void recordComponentsShouldBeImmutable() {
        // Arrange
        TargetSource targetSource = new TargetSource(new Object());
        MethodInterceptor interceptor = (proceed) -> {
            return proceed.get();
        };
        MethodMatcher matcher = new AspectJExpressionPointcut("execution(* com.pythongong.test.utils.TestBean.*(..))");

        // Act
        AdvisedSupport advised = new AdvisedSupport(targetSource, interceptor,
                matcher);

        // Create new components
        TargetSource newTargetSource = new TargetSource(new Object());
        MethodInterceptor newInterceptor = (proceed) -> {
            return proceed.get();
        };
        MethodMatcher newMatcher = new AspectJExpressionPointcut(
                "execution(* com.pythongong.test.utils.TestBean.*(..))");

        // Assert - components should remain unchanged
        assertSame(targetSource, advised.targetSource());
        assertSame(interceptor, advised.methodInterceptor());
        assertSame(matcher, advised.matcher());

        // Verify they're different from new components
        assertNotSame(newTargetSource, advised.targetSource());
        assertNotSame(newInterceptor, advised.methodInterceptor());
        assertNotSame(newMatcher, advised.matcher());
    }
}