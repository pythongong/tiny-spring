package com.pythongong.aop;

import org.junit.jupiter.api.Test;

import com.pythongong.test.aop.valid.AopTestInterface;
import com.pythongong.test.aop.valid.AopTestTarget;

import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class AdvisedSupportTests {

    static final String CORRECT_EXPRESS = "execution(* com.pythongong.test.aop.AopTestInterface.*(..))";

    @Test
    @DisplayName("Constructor should create valid instance with all parameters")
    void constructorShouldCreateValidInstance() {
        // Arrange

        MethodInterceptor interceptor = (invocation) -> {
            return invocation.proceed();
        };

        AopTestInterface targetSource = new AopTestTarget();
        // Act
        AdvisedSupport advised = AdvisedSupport.builder()
                .target(targetSource)
                .methodInterceptor(interceptor)

                .build();

        // Assert
        assertNotNull(advised);
        assertEquals(targetSource, advised.target());
        assertEquals(interceptor, advised.methodInterceptor());
    }

    @Test
    @DisplayName("Constructor should throw exception when targetSource is null")
    void constructorShouldThrowExceptionWhenTargetSourceIsNull() {
        // Arrange
        MethodInterceptor interceptor = (invocation) -> {
            return invocation.proceed();
        };

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> AdvisedSupport.builder()
                        .target(null)
                        .methodInterceptor(interceptor)

                        .build());
    }

    @Test
    @DisplayName("Constructor should throw exception when methodInterceptor is null")
    void constructorShouldThrowExceptionWhenMethodInterceptorIsNull() {
        // Arrange
        AopTestInterface targetSource = new AopTestTarget();

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> AdvisedSupport.builder()
                        .target(targetSource)
                        .methodInterceptor(null)

                        .build());
    }

    @Test
    @DisplayName("Record components should be immutable")
    void recordComponentsShouldBeImmutable() {
        // Arrange
        AopTestInterface targetSource = new AopTestTarget();
        MethodInterceptor interceptor = (invocation) -> {
            return invocation.proceed();
        };

        // Act
        AdvisedSupport advised = AdvisedSupport.builder()
                .target(targetSource)
                .methodInterceptor(interceptor)

                .build();
        // Create new components
        AopTestInterface newTargetSource = new AopTestTarget();
        MethodInterceptor newInterceptor = (invocation) -> {
            return invocation.proceed();
        };

        // Assert - components should remain unchanged
        assertSame(targetSource, advised.target());
        assertSame(interceptor, advised.methodInterceptor());

        // Verify they're different from new components
        assertNotSame(newTargetSource, advised.target());
        assertNotSame(newInterceptor, advised.methodInterceptor());

    }
}