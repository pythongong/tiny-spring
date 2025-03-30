package com.pythongong.aop.aspectj;

import org.junit.jupiter.api.Test;

import com.pythongong.test.aop.valid.AopTestInterface;
import com.pythongong.test.aop.valid.DuplicateAopTestInterface;

import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;

/**
 * @author Cheng Gong
 */
class AspectJExpressionPointcutTests {

    private static final String CORRECT_EXPRESS = "execution(* com.pythongong.test.aop.valid.AopTestInterface.*(..))";

    @Test
    @DisplayName("Should match execution of public methods")
    void shouldMatchExecutionOfPublicMethods() throws Exception {
        // Arrange
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(CORRECT_EXPRESS);
        Method method = AopTestInterface.class.getMethod("getProxy");

        // Act & Assert
        assertTrue(pointcut.matches(method));
    }

    @Test
    @DisplayName("Should not match methods from different class")
    void shouldNotMatchMethodsFromDifferentClass() throws Exception {
        // Arrange
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
                CORRECT_EXPRESS);
        Method method = DuplicateAopTestInterface.class.getMethod("getProxy");

        // Act & Assert
        assertFalse(pointcut.matches(method));
    }

    @Test
    @DisplayName("Should match methods with specific parameter types")
    void shouldMatchMethodsWithSpecificParameterTypes() throws Exception {
        // Arrange
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
                CORRECT_EXPRESS);
        Method method = AopTestInterface.class.getMethod("add", int.class,
                int.class);

        // Act & Assert

        assertTrue(pointcut.matches(method));
    }

    @Test
    @DisplayName("Should throw exception for invalid expression")
    void shouldThrowExceptionForInvalidExpression() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> new AspectJExpressionPointcut("invalid expression"));
    }

}