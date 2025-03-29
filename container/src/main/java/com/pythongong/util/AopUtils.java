package com.pythongong.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;

import com.pythongong.aop.advice.JoinPoint;
import com.pythongong.aop.advice.ProceedingJoinPoint;
import com.pythongong.aop.aspectj.AspectJExpressionPointcut;
import com.pythongong.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.pythongong.aop.autoproxy.AspectJAutoProxyCreator;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.FieldValue;
import com.pythongong.beans.config.FieldValueList;
import com.pythongong.enums.AdviceEnum;
import com.pythongong.exception.AopConfigException;

public class AopUtils {
    private AopUtils() {
    }

    public static BeanDefinition definiteAopBeanPostProcessor() {
        Class<AspectJAutoProxyCreator> beanClass = AspectJAutoProxyCreator.class;
        return BeanDefinition.builder()
                .beanClass(beanClass)
                .beanName(beanClass.getName())
                .build();
    }

    @SuppressWarnings("unchecked")
    public static void addAdvisors(BeanDefinition aspectDefinition, BeanDefinition aopBeanPostProcessorDefinition) {
        Class<?> aspectClass = aspectDefinition.beanClass();
        String apsectName = aspectDefinition.beanName();
        List<AspectJExpressionPointcutAdvisor> advisors = new ArrayList<>();
        for (Method method : aspectClass.getMethods()) {
            AspectJExpressionPointcutAdvisor advisor = createAdvisor(apsectName, method);
            if (advisor != null) {
                advisors.add(advisor);
            }
        }
        FieldValueList fieldValueList = aopBeanPostProcessorDefinition.fieldValueList();
        FieldValue fieldValue = fieldValueList.getFieldValue("advisors");
        if (fieldValue == null) {
            fieldValueList.add(new FieldValue("advisors", advisors));
            return;
        }
        List<AspectJExpressionPointcutAdvisor> oldAdvisors = (List<AspectJExpressionPointcutAdvisor>) fieldValue
                .value();
        oldAdvisors.addAll(advisors);
    }

    private static AspectJExpressionPointcutAdvisor createAdvisor(String apsectName, Method method) {
        AdviceEnum adviceEnum = null;
        Annotation adviceAnnotation = null;
        for (Annotation annotation : method.getAnnotations()) {
            AdviceEnum curEnum = AdviceEnum.fromAnnotation(annotation.annotationType());
            if (curEnum != null) {
                adviceEnum = curEnum;
                adviceAnnotation = annotation;
            }
        }
        if (adviceEnum == null) {
            return null;
        }

        String expression = null;
        switch (adviceEnum) {
            case Before:
                expression = ((Before) adviceAnnotation).value();
                checkBeforeAndAfterArgus(method);
                break;
            case After:
                expression = ((After) adviceAnnotation).value();
                checkBeforeAndAfterArgus(method);
                break;
            case Around:
                expression = ((Around) adviceAnnotation).value();
                checkAroundAgurs(method);
                break;
            case AfterReturning:
                expression = ((AfterReturning) adviceAnnotation).value();
                checkAfterReturning(method);
                break;
            default:
                break;
        }

        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut(expression);
        return AspectJExpressionPointcutAdvisor.builder()
                .method(method)
                .aspectName(apsectName)
                .adviceEnum(adviceEnum)
                .pointcut(pointcut)
                .build();
    }

    private static void checkAfterReturning(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (ClassUtils.isArrayEmpty(parameterTypes) || parameterTypes.length != 2
                || parameterTypes[0] != JoinPoint.class || parameterTypes[1] != Object.class) {
            throw new AopConfigException("");
        }
    }

    private static void checkAroundAgurs(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (ClassUtils.isArrayEmpty(parameterTypes) || parameterTypes.length != 1
                || parameterTypes[0] != ProceedingJoinPoint.class) {
            throw new AopConfigException("");
        }
    }

    private static void checkBeforeAndAfterArgus(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (ClassUtils.isArrayEmpty(parameterTypes)) {
            return;
        }

        if (parameterTypes.length != 1 || parameterTypes[0] != JoinPoint.class) {
            throw new AopConfigException("");
        }
    }

}
