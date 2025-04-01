/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pythongong.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Before;

import com.pythongong.aop.JoinPoint;
import com.pythongong.aop.ProceedingJoinPoint;
import com.pythongong.aop.aspectj.AspectJExpressionPointcut;
import com.pythongong.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.pythongong.aop.autoproxy.AspectJAutoProxyCreator;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.FieldValue;
import com.pythongong.beans.config.FieldValueList;
import com.pythongong.enums.AdviceEnum;
import com.pythongong.exception.AopConfigException;

/**
 * Utility class providing AOP-related helper methods.
 * 
 * <p>Contains methods for handling aspect definitions, advisor creation,
 * and advice invocation in the AOP framework.
 *
 * @author pythongong
 * @since 1.0
 */
public class AopUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private AopUtils() {
    }

    /**
     * Creates a bean definition for the AOP bean post processor.
     * @return the created bean definition
     */
    public static BeanDefinition definiteAopBeanPostProcessor() {
        Class<AspectJAutoProxyCreator> beanClass = AspectJAutoProxyCreator.class;
        return BeanDefinition.builder()
                .beanClass(beanClass)
                .beanName(AspectJAutoProxyCreator.BEAN_NAME)
                .build();
    }

    /**
     * Adds advisors to the AOP bean post processor definition.
     * @param aspectDefinition the aspect bean definition
     * @param aopBeanPostProcessorDefinition the AOP post processor definition
     */
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

    /**
     * Creates an advisor from the given aspect method.
     * @param apsectName the name of the aspect
     * @param method the method to create advisor from
     * @return the created advisor, or null if method is not an advice
     */
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

    /**
     * Validates the method signature for after returning advice.
     * @param method the method to validate
     * @throws AopConfigException if the method signature is invalid
     */
    private static void checkAfterReturning(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (ClassUtils.isArrayEmpty(parameterTypes) || parameterTypes.length != 2
                || parameterTypes[0] != JoinPoint.class || parameterTypes[1] != Object.class) {
            throw new AopConfigException("");
        }
    }

    /**
     * Validates the method signature for around advice.
     * @param method the method to validate
     * @throws AopConfigException if the method signature is invalid
     */
    private static void checkAroundAgurs(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (ClassUtils.isArrayEmpty(parameterTypes) || parameterTypes.length != 1
                || parameterTypes[0] != ProceedingJoinPoint.class) {
            throw new AopConfigException("");
        }
        if (method.getReturnType() == void.class) {
            throw new AopConfigException("");
        }
    }

    /**
     * Validates the method signature for before and after advice.
     * @param method the method to validate
     * @throws AopConfigException if the method signature is invalid
     */
    private static void checkBeforeAndAfterArgus(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (ClassUtils.isArrayEmpty(parameterTypes)) {
            return;
        }

        if (parameterTypes.length != 1 || parameterTypes[0] != JoinPoint.class) {
            throw new AopConfigException("");
        }
    }

    /**
     * Invokes the advice method with the appropriate parameters.
     * @param param the parameters for advice invocation
     * @return the result of advice invocation
     * @throws AopConfigException if advice invocation fails
     */
    public static Object invokeAdvice(AdviceMethodParam param) {
        Method advicMethod = param.advicMethod();
        Class<?>[] parameterTypes = advicMethod.getParameterTypes();
        Object[] argus = new Object[parameterTypes.length];
        if (argus.length > 0) {
            if (parameterTypes[0] == JoinPoint.class) {
                argus[0] = param.joinPoint();
            }

            if (parameterTypes[0] == ProceedingJoinPoint.class) {
                argus[0] = param.proceedingJoinPoint();
            }

        }
        if (argus.length > 1) {
            argus[1] = param.retVal();
        }

        try {
            advicMethod.setAccessible(true);
            return advicMethod.invoke(param.aspect(), argus);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AopConfigException(String.format("{%s} advice failed", advicMethod.getName()));
        }
    }
}
