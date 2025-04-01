package com.pythongong.aop.autoproxy;

import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pythongong.aop.JoinPoint;
import com.pythongong.aop.aspectj.AspectJExpressionPointcut;
import com.pythongong.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.impl.DefaultListableBeanFactory;
import com.pythongong.enums.AdviceEnum;
import com.pythongong.test.aop.valid.AopTestTarget;
import com.pythongong.test.aop.valid.TestAspect;

class AspectJAutoProxyCreatorTest {

    private AspectJAutoProxyCreator proxyCreator;
    private DefaultListableBeanFactory beanFactory;

    @BeforeEach
    void setUp() {
        proxyCreator = new AspectJAutoProxyCreator();
        beanFactory = new DefaultListableBeanFactory();
        proxyCreator.setBeanFactory(beanFactory);
    }

    @Test
    void shouldReturnOriginalBeanWhenNoAdvisors() {
        AopTestTarget target = new AopTestTarget();
        Object result = proxyCreator.create(target, "testService");
        assertSame(target, result);
    }

    @Test
    void shouldReturnOriginalBeanWhenBeanIsAspect() {
        TestAspect testAspect = new TestAspect();
        Object result = proxyCreator.create(testAspect, "testAspect");
        assertSame(testAspect, result);
    }

    @Test
    void shouldCreateProxyWhenMatchingAdvisorExists() throws NoSuchMethodException {
        // Setup test objects
        AopTestTarget testService = new AopTestTarget();
        beanFactory.registerBeanDefinition(BeanDefinition.builder()
                .beanClass(TestAspect.class)
                .beanName("testAspect")
                .build());
        Method adviceMethod = TestAspect.class.getMethod("beforeAdvice", JoinPoint.class);

        // Setup pointcut and advisor
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(
                "execution(* com.pythongong.test.aop.valid.AopTestTarget.*(..))");
        AspectJExpressionPointcutAdvisor advisor = AspectJExpressionPointcutAdvisor.builder()
                .aspectName("testAspect")
                .adviceEnum(AdviceEnum.Before)
                .method(adviceMethod)
                .pointcut(pointcut)
                .build();

        // Set advisors through reflection
        try {
            java.lang.reflect.Field advisorsField = AspectJAutoProxyCreator.class.getDeclaredField("advisors");
            advisorsField.setAccessible(true);
            advisorsField.set(proxyCreator, List.of(advisor));
        } catch (Exception e) {
            fail("Failed to set advisors field");
        }

        // Test proxy creation
        Object result = proxyCreator.create(testService, "testService");

        assertNotNull(result);
        assertNotSame(testService, result);
        assertTrue(result instanceof AopTestTarget);
    }

}