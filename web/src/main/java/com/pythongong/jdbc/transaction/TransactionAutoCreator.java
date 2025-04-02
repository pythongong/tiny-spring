package com.pythongong.jdbc.transaction;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.sql.DataSource;

import org.aspectj.lang.annotation.Aspect;

import com.pythongong.annotation.Transactional;
import com.pythongong.aop.AdvisedSupport;
import com.pythongong.aop.autoproxy.AutoProxyCreator;
import com.pythongong.aop.interceptor.MethodInterceptor;
import com.pythongong.aop.interceptor.MethodMatcherInterceptor;
import com.pythongong.aop.proxy.ProxyFactory;
import com.pythongong.beans.factory.BeanFactory;
import com.pythongong.exception.AopConfigException;
import com.pythongong.exception.BeansException;

public class TransactionAutoCreator implements AutoProxyCreator {

    private static final String DEFAULT_MANAGER = "platformTransactionManager";

    private final DataSource dataSource;

    public TransactionAutoCreator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    private BeanFactory beanFactory;

    @Override
    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object create(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        if (bean instanceof AutoProxyCreator || beanClass.isAnnotationPresent(Aspect.class)) {
            return bean;
        }
        List<Method> transactionMethods = getTransactionMethod(beanClass);
        if (transactionMethods.isEmpty()) {
            return bean;
        }
        AdvisedSupport advisedSupport = AdvisedSupport.builder()
                .methodMatcherInterceptors(createMatcherInterceptors(transactionMethods))
                .target(bean)
                .build();
        return ProxyFactory.createProxy(advisedSupport);
    }

    private List<MethodMatcherInterceptor> createMatcherInterceptors(List<Method> transactionMethods) {
        return transactionMethods.stream().map(transactionMethod -> {
            Transactional annotation = transactionMethod.getAnnotation(Transactional.class);
            MethodInterceptor methodInterceptor = null;
            String managerName = annotation.value();
            if (DEFAULT_MANAGER.equals(managerName)) {
                methodInterceptor = new DataSourceTransactionManager(dataSource);
            } else {
                methodInterceptor = (MethodInterceptor) beanFactory.getBean(managerName);
            }
            if (methodInterceptor == null) {
                throw new AopConfigException(managerName + " doesn't exist");
            }
            return new MethodMatcherInterceptor(methodInterceptor, (method) -> {
                return transactionMethod.equals(method);
            });
        }).toList();
    }

    private List<Method> getTransactionMethod(Class<?> beanClass) {
        Method[] methods = beanClass.getMethods();
        if (beanClass.isAnnotationPresent(Transactional.class)) {
            return Arrays.asList(methods);
        }
        List<Method> transactionMethods = new ArrayList<>(methods.length);
        Arrays.stream(methods).forEach(method -> {
            if (method.isAnnotationPresent(Transactional.class)) {
                transactionMethods.add(method);
            }
        });
        return transactionMethods;
    }
}
