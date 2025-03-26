package com.pythongong.aop;

import org.junit.jupiter.api.Test;

import com.pythongong.aop.aspectj.AspectJExpressionPointcut;
import com.pythongong.test.aop.AopTestInterface;
import com.pythongong.test.aop.AopTestTarget;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class AdvisedSupportTests {

    static final String CORRECT_EXPRESS = "execution(* com.pythongong.test.aop.AopTestInterface.*(..))";

    @Test
    @DisplayName("Constructor should create valid instance with all parameters")
    void constructorShouldCreateValidInstance() {
        // Arrange

        MethodInterceptor interceptor = (proceed) -> {
            return proceed.get();
        };
        MethodMatcher matcher = new AspectJExpressionPointcut(CORRECT_EXPRESS);

        AopTestInterface targetSource = new AopTestTarget();
        // Act
        AdvisedSupport advised = new AdvisedSupport(targetSource, interceptor, matcher);

        // Assert
        assertNotNull(advised);
        assertEquals(targetSource, advised.target());
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
        MethodMatcher matcher = new AspectJExpressionPointcut(CORRECT_EXPRESS);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AdvisedSupport(null, interceptor, matcher));
    }

    @Test
    @DisplayName("Constructor should throw exception when methodInterceptor is null")
    void constructorShouldThrowExceptionWhenMethodInterceptorIsNull() {
        // Arrange
        AopTestInterface targetSource = new AopTestTarget();
        MethodMatcher matcher = new AspectJExpressionPointcut(CORRECT_EXPRESS);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AdvisedSupport(targetSource, null, matcher));
    }

    @Test
    @DisplayName("Constructor should throw exception when matcher is null")
    void constructorShouldThrowExceptionWhenMatcherIsNull() {
        // Arrange
        AopTestInterface targetSource = new AopTestTarget();
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
        AopTestInterface targetSource = new AopTestTarget();
        MethodInterceptor interceptor = (proceed) -> {
            return proceed.get();
        };
        MethodMatcher matcher = new AspectJExpressionPointcut(CORRECT_EXPRESS);

        // Act
        AdvisedSupport advised = new AdvisedSupport(targetSource, interceptor,
                matcher);

        // Create new components
        AopTestInterface newTargetSource = new AopTestTarget();
        MethodInterceptor newInterceptor = (proceed) -> {
            return proceed.get();
        };
        MethodMatcher newMatcher = new AspectJExpressionPointcut(
                CORRECT_EXPRESS);

        // Assert - components should remain unchanged
        assertSame(targetSource, advised.target());
        assertSame(interceptor, advised.methodInterceptor());
        assertSame(matcher, advised.matcher());

        // Verify they're different from new components
        assertNotSame(newTargetSource, advised.target());
        assertNotSame(newInterceptor, advised.methodInterceptor());
        assertNotSame(newMatcher, advised.matcher());
    }
}