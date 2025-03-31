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

package com.pythongong.aop.autoproxy;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.Aspect;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.aspectj.AspectJExpressionPointcut;
import com.pythongong.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.pythongong.aop.interceptor.AdviceInterceptorFactory;
import com.pythongong.aop.interceptor.AdviceInterceptorParam;
import com.pythongong.aop.interceptor.MethodMatcherInterceptor;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.aop.proxy.ProxyFactory;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.enums.AdviceEnum;
import com.pythongong.exception.AopConfigException;
import com.pythongong.exception.BeansException;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.StringUtils;

/**
 * BeanPostProcessor implementation that creates AOP proxies based on detected
 * AspectJ-annotated classes in Spring beans.
 *
 * <p>
 * Processes beans to create AOP proxies that implement the specific aspect
 * contracts,
 * delegating to the given AspectJ aspects for the actual aspect implementation.
 *
 * @author pythongong
 * @since 1.0
 */
public class AspectJAutoProxyCreator {

    public static final String BEAN_NAME = AspectJAutoProxyCreator.class.getName();

    /** The bean factory used for resolving aspect bean references */
    private DefaultListableBeanFactory beanFactory;

    /** List of AspectJ advisors */
    private List<AspectJExpressionPointcutAdvisor> advisors;

    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public void setBeanFactory(DefaultListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    /**
     * Create a proxy with AOP support if necessary.
     * 
     * @param bean     the new bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one
     * @throws BeansException in case of errors
     */

    public Object create(Object bean, String beanName) throws BeansException {
        // Skip if no advisors available or invalid bean
        if (ClassUtils.isCollectionEmpty(advisors) || bean == null || StringUtils.isEmpty(beanName)) {
            return bean;
        }

        // Skip proxy creation for AspectJ classes and the proxy creator itself
        Class<?> beanClass = bean.getClass();
        if (beanClass == AspectJAutoProxyCreator.class || beanClass.isAnnotationPresent(Aspect.class)) {
            return bean;
        }

        // Find and sort advisors that match the current bean class
        List<AspectJExpressionPointcutAdvisor> relatedAdvisors = advisors.stream()
                .filter(advisor -> {
                    return advisor.pointcut().matchesClass(beanClass);
                }).sorted((advisor1, advisor2) -> {
                    return AdviceEnum.compare(advisor1.adviceEnum(), advisor2.adviceEnum());
                }).toList();

        if (relatedAdvisors.isEmpty()) {
            return bean;
        }

        // Create and return the proxy if advisors are found
        return ProxyFactory.createProxy(AdvisedSupport
                .builder()
                .methodMatcherInterceptors(createMethodMatcherInterceptors(relatedAdvisors))
                .target(bean)
                .build());
    }

    /**
     * Creates method interceptors for each advisor that matches the target class.
     * 
     * @param relatedAdvisors list of advisors that match the target class
     * @return list of method matcher interceptors
     * @throws AopConfigException if advice creation fails
     */
    private List<MethodMatcherInterceptor> createMethodMatcherInterceptors(
            List<AspectJExpressionPointcutAdvisor> relatedAdvisors) {

        List<MethodMatcherInterceptor> methodMatcherInterceptors = new ArrayList<>();

        relatedAdvisors.forEach(advisor -> {
            // Get the aspect instance from bean factory
            Object aspect = beanFactory.getBean(advisor.aspectName());
            MethodInterceptor methodInterceptor = null;

            // Create advice interceptor with aspect instance and method
            AdviceInterceptorParam param = new AdviceInterceptorParam(aspect, advisor.method());
            methodInterceptor = AdviceInterceptorFactory.createAdvice(param, advisor.adviceEnum());
            if (methodInterceptor == null) {
                throw new AopConfigException("Unknow advoce enum: " + advisor.adviceEnum().name());
            }

            // Create and add method matcher interceptor
            AspectJExpressionPointcut pointcut = advisor.pointcut();
            methodMatcherInterceptors.add(new MethodMatcherInterceptor(
                    methodInterceptor, pointcut.methodMatcher()));
        });

        return methodMatcherInterceptors;
    }
}
