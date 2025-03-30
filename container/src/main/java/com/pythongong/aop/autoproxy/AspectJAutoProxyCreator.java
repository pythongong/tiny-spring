package com.pythongong.aop.autoproxy;

import java.util.ArrayList;
import java.util.List;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.aspectj.AspectJExpressionPointcut;
import com.pythongong.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.pythongong.aop.interceptor.AfterMethodInterceptor;
import com.pythongong.aop.interceptor.AfterReturningMethodInterceptor;
import com.pythongong.aop.interceptor.AroundMethodInterceptor;
import com.pythongong.aop.interceptor.BeforeMethodInterceptor;
import com.pythongong.aop.interceptor.DynamicMatchMethodInterceptor;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.aop.proxy.ProxyFactory;
import com.pythongong.beans.aware.BeanFactoryAware;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.factory.BeanFactory;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.enums.AdviceEnum;
import com.pythongong.exception.AopConfigException;
import com.pythongong.exception.BeansException;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.StringUtils;

public class AspectJAutoProxyCreator implements BeanPostProcessor, BeanFactoryAware {

    private DefaultListableBeanFactory beanFactory;

    private List<AspectJExpressionPointcutAdvisor> advisors;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (ClassUtils.isCollectionEmpty(advisors) || bean == null || StringUtils.isEmpty(beanName)) {
            return bean;
        }

        Class<?> beanClass = bean.getClass();
        List<AspectJExpressionPointcutAdvisor> relatedAdvisors = advisors.stream()
                .filter(advisor -> {
                    return advisor.pointcut().matchesClass(beanClass);
                }).toList();
        if (relatedAdvisors.isEmpty()) {
            return bean;
        }
        List<MethodInterceptor> methodInterceptors = createInterceptors(relatedAdvisors);
        return ProxyFactory.createProxy(AdvisedSupport
                .builder()
                .methodInterceptors(methodInterceptors)
                .target(bean)
                .build());
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    private List<MethodInterceptor> createInterceptors(List<AspectJExpressionPointcutAdvisor> relatedAdvisors) {

        relatedAdvisors.sort((advisor1, advisor2) -> {
            return AdviceEnum.compare(advisor1.adviceEnum(), advisor2.adviceEnum());
        });

        List<MethodInterceptor> beforeInterceptors = new ArrayList<>();
        relatedAdvisors.forEach(advisor -> {
            Object aspect = beanFactory.getBean(advisor.aspectName());
            AspectJExpressionPointcut pointcut = advisor.pointcut();
            MethodInterceptor methodInterceptor = null;
            switch (advisor.adviceEnum()) {
                case Before:
                    methodInterceptor = new BeforeMethodInterceptor(advisor.method(), aspect);
                    break;
                case Around:
                    methodInterceptor = new AroundMethodInterceptor(advisor.method(), aspect);
                    break;
                case After:
                    methodInterceptor = new AfterMethodInterceptor(advisor.method(), aspect);
                    break;
                case AfterReturning:
                    methodInterceptor = new AfterReturningMethodInterceptor(advisor.method(), aspect);
                    break;
                default:
                    break;
            }

            if (methodInterceptor == null) {
                throw new AopConfigException("");
            }

            if (!pointcut.isDynamic()) {
                beforeInterceptors.add(methodInterceptor);
            } else {
                beforeInterceptors.add(new DynamicMatchMethodInterceptor(
                        methodInterceptor, pointcut.methodMatcher()));
            }
        });
        return beforeInterceptors;
    }
}
