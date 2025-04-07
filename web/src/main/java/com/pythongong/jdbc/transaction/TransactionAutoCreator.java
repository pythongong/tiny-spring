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
import com.pythongong.enums.TransactionIsolationLevel;
import com.pythongong.exception.BeansException;

/**
 * Creates AOP proxies for transactional methods automatically.
 * 
 * <p>This class is responsible for creating proxy objects that add transaction
 * management capabilities to beans with {@code @Transactional} annotations.
 * It scans beans for transaction annotations and creates appropriate interceptors
 * for transaction management.
 *
 * <p>The creator supports both class-level and method-level @Transactional
 * annotations and configures transaction isolation levels as specified.
 *
 * @author pythongong
 * @since 1.0
 * @see com.pythongong.annotation.Transactional
 * @see com.pythongong.aop.autoproxy.AutoProxyCreator
 */
public class TransactionAutoCreator implements AutoProxyCreator {

    /** Default name for the platform transaction manager bean */
    private static final String DEFAULT_MANAGER = "platformTransactionManager";

    /** DataSource for creating transaction managers */
    private final DataSource dataSource;

    /** Reference to the bean factory */
    private BeanFactory beanFactory;

    /**
     * Creates a new transaction proxy creator with the specified data source.
     *
     * @param dataSource the data source for transaction management
     */
    public TransactionAutoCreator(DataSource dataSource) {
        this.dataSource = dataSource;
    }

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
        List<MethodMatcherInterceptor> methodMatcherInterceptors = new ArrayList<>(transactionMethods.size());
        transactionMethods.forEach(transactionMethod -> {
            Transactional annotation = transactionMethod.getAnnotation(Transactional.class);
            MethodInterceptor methodInterceptor = null;
            String managerName = annotation.value();
            TransactionIsolationLevel level = annotation.level();
            if (!DEFAULT_MANAGER.equals(managerName)) {
                return;
            }
            methodInterceptor = new DataSourceTransactionManager(dataSource, level);
            methodMatcherInterceptors.add(new MethodMatcherInterceptor(methodInterceptor, (method) -> {
                return transactionMethod.equals(method);
            }));
        });
        return methodMatcherInterceptors;
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
