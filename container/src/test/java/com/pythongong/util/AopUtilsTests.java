package com.pythongong.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pythongong.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.pythongong.aop.autoproxy.AspectJAutoProxyCreator;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.FieldValue;
import com.pythongong.exception.AopConfigException;
import com.pythongong.test.aop.invalid.InvalidBeforeAdvice;
import com.pythongong.test.aop.valid.TestAspect;

class AopUtilsTests {

    @Test
    @DisplayName("Should create AOP bean post processor definition")
    void shouldCreateAopBeanPostProcessorDefinition() {
        BeanDefinition aopBeanPostProcessor = AopUtils.definiteAopBeanPostProcessor();
        assertNotNull(aopBeanPostProcessor);
        assertEquals(AspectJAutoProxyCreator.class.getName(), aopBeanPostProcessor.beanName());
        assertEquals(AspectJAutoProxyCreator.class, aopBeanPostProcessor.beanClass());
    }

    @Test
    @DisplayName("Should add advisors from aspect class")
    void shouldAddAdvisorsFromAspectClass() {
        // Arrange
        BeanDefinition aspectDefinition = BeanDefinition.builder()
                .beanClass(TestAspect.class)
                .beanName("testAspect")
                .build();

        BeanDefinition aopDefinition = AopUtils.definiteAopBeanPostProcessor();

        // Act
        AopUtils.addAdvisors(aspectDefinition, aopDefinition);

        // Assert
        FieldValue fieldValue = aopDefinition.fieldValueList().getFieldValue("advisors");
        assertNotNull(fieldValue);

        @SuppressWarnings("unchecked")
        List<AspectJExpressionPointcutAdvisor> advisors = (List<AspectJExpressionPointcutAdvisor>) fieldValue.value();
        assertEquals(4, advisors.size()); // Should have all 4 advice methods
    }

    @Test
    @DisplayName("Should throw exception for invalid Before advice parameters")
    void shouldThrowExceptionForInvalidBeforeAdviceParameters() {
        // Arrange
        BeanDefinition aspectDefinition = BeanDefinition.builder()
                .beanClass(InvalidBeforeAdvice.class)
                .beanName("invalidAspect")
                .build();
        // Act & Assert
        assertThrows(AopConfigException.class, () -> {
            AopUtils.addAdvisors(aspectDefinition, AopUtils.definiteAopBeanPostProcessor());
        });
    }

}