/*
 * Copyright 2025 Cheng Gong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pythongong.beans.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.aop.autoproxy.AutoProxyCreator;
import com.pythongong.beans.aware.Aware;
import com.pythongong.beans.aware.BeanClassLoaderAware;
import com.pythongong.beans.aware.BeanFactoryAware;
import com.pythongong.beans.aware.BeanNameAware;
import com.pythongong.beans.config.AfterInitializationpProcessor;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanProcessor;
import com.pythongong.beans.config.BeanReference;
import com.pythongong.beans.config.BeforeInitializationProcessor;
import com.pythongong.beans.config.DisposableBean;
import com.pythongong.beans.config.FactoryDefinition;
import com.pythongong.beans.config.InitializingBean;
import com.pythongong.beans.config.FieldValue;
import com.pythongong.beans.config.FieldValueList;
import com.pythongong.beans.factory.ConfigurableListableBeanFactory;
import com.pythongong.beans.registry.BeanDefinitionRegistry;
import com.pythongong.context.event.ApplicationEventMulticaster;
import com.pythongong.context.event.GeneralApplicationEventMulticaster;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.exception.BeansException;
import com.pythongong.exception.NoSuchBeanException;
import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Nullable;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.StringUtils;

/**
 * Default implementation of the ConfigurableListableBeanFactory and
 * BeanDefinitionRegistry interfaces.
 * This is a complete bean factory implementation that supports singleton and
 * prototype beans,
 * Aware interfaces, lifecycle methods, property injection, and bean post
 * processing.
 *
 * @author Cheng Gong
 */

public class DefaultListableBeanFactory
        implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    /** Map of bean definitions, keyed by bean name */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(ClassUtils.BIG_INIT_SIZE);

    /** List of bean post processors */
    private final List<BeanProcessor> beanProcessors = new ArrayList<>(ClassUtils.SMALL_INIT_SIZE);

    /** Strategy for creating bean instances */
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiation();

    /** Delegate for core bean factory operations */
    private final GeneralBeanFactory generalBeanFactory;

    /** Registry for singleton beans */
    private final DefaultSingletonBeanRegistry singletonBeanRegistry;

    /**
     * Creates a new DefaultListableBeanFactory.
     * Initializes the singleton registry and general bean factory.
     */
    public DefaultListableBeanFactory() {
        singletonBeanRegistry = new DefaultSingletonBeanRegistry();
        generalBeanFactory = new GeneralBeanFactory(this::getBeanDefinition, this::createBean, singletonBeanRegistry);
    }

    @Nullable
    public List<BeanDefinition> getBeanDefinitionsOfType(Class<?> requiredType) throws BeansException {
        CheckUtils.nullArgs(requiredType,
                "DefaultListableBeanFactory.getBeanDefinitionsOfType recevies null bean class");
        return beanDefinitionMap.values().stream()
                .filter(beanDefinition -> requiredType.isAssignableFrom(beanDefinition.beanClass()))
                .toList();
    }

    @Override
    @Nullable
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        CheckUtils.emptyString(beanName, "DefaultListableBeanFactory.getBeanDefinition recevies empty bean name");
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        CheckUtils.nullArgs(beanDefinition,
                "DefaultListableBeanFactory.registerBeanDefinition recevies null bean definition");
        beanDefinitionMap.put(beanDefinition.beanName(), beanDefinition);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        CheckUtils.nullArgs(type, "DefaultListableBeanFactory.getBeansOfType recevies null bean type");
        Map<String, T> results = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (type.isAssignableFrom(beanDefinition.beanClass())) {
                results.put(beanName, (T) getBean(beanName));
            }
        });

        return results;
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }

    @Nullable
    @Override
    public Object getBean(String name) throws BeansException {
        return this.generalBeanFactory.getBean(name);
    }

    @Nullable
    @Override
    public Object getSingleton(String beanName) {
        return singletonBeanRegistry.getSingleton(beanName);
    }

    @Override
    public void destroySingletons() {
        singletonBeanRegistry.destroySingletons();
    }

    @Override
    public void addBeanProcessor(BeanProcessor beanProcessor) {
        CheckUtils.nullArgs(beanProcessor, "GeneralBeanFactory.addbeanProcessor recevies null processor");
        if (!beanProcessors.contains(beanProcessor)) {
            beanProcessors.add(beanProcessor);
        }
    }

    @Nullable
    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return generalBeanFactory.getBean(name, requiredType);
    }

    @Override
    public ApplicationEventMulticaster initApplicationEventMulticaster() {
        GeneralApplicationEventMulticaster applicationEventMulticaster = new GeneralApplicationEventMulticaster();
        singletonBeanRegistry.registerSingleton(ClassUtils.APPLICATION_EVENT_MULTICASTER_BEAN_NAME,
                applicationEventMulticaster);
        return applicationEventMulticaster;
    }

    /**
     * Creates a new instance of a bean from its definition.
     *
     * @param beanDefinition the bean definition to create an instance from
     * @return the created bean instance
     */
    private Object createBean(BeanDefinition beanDefinition) throws BeansException {
        Object bean;
        String beanName = beanDefinition.beanName();

        bean = createBeanInstance(beanDefinition);

        // Avoid the cylclic dependency problem
        if (ScopeEnum.SINGLETON.equals(beanDefinition.scope())) {
            singletonBeanRegistry.registerSingleton(beanName, getEarlyBeanReference(bean, beanName));
        }

        bean = initializeBean(bean, beanDefinition);

        registerDisposableBeanIfNecessary(bean, beanDefinition);
        if (!ScopeEnum.SINGLETON.equals(beanDefinition.scope())) {
            return bean;
        }
        Object singleton = singletonBeanRegistry.getSingleton(beanName);
        if (singleton == null) {
            singleton = bean;
            singletonBeanRegistry.registerSingleton(beanName, bean);
        }
        return singleton;
    }

    /**
     * Get the empty reference for the given bean to avoid the cyclic dependency
     * problem.
     * If the bean is an autoProxyCreator, it returns the bean itself.
     * Otherwise, it gets an autoProxyCreator.
     * If an autoProxyCreator is not found, it returns the bean itself.
     * and sets its bean factory to create the proxy.
     * 
     * @param bean     the bean instance
     * @param beanName the name of the bean
     * @return the empty reference for the bean or a proxy reference of the bean
     */
    private Object getEarlyBeanReference(Object bean, String beanName) {
        if (bean instanceof AutoProxyCreator) {
            return bean;
        }

        for (BeanProcessor beanProcessor : beanProcessors) {
            if (beanProcessor instanceof AutoProxyCreator) {
                AutoProxyCreator creator = (AutoProxyCreator) beanProcessor;
                if (creator.getBeanFactory() == null) {
                    creator.setBeanFactory(generalBeanFactory);
                }
                Object curBean = creator.create(bean, beanName);
                if (curBean != null) {
                    bean = curBean;
                }
            }
        }

        return bean;
    }

    /**
     * Registers a bean as disposable if it implements DisposableBean
     * or has a custom destroy method.
     */
    private void registerDisposableBeanIfNecessary(Object bean, BeanDefinition beanDefinition) {
        if (!ScopeEnum.SINGLETON.equals(beanDefinition.scope())) {
            return;
        }

        if (bean instanceof DisposableBean) {
            singletonBeanRegistry.registerDisposableBean(beanDefinition.beanName(), (DisposableBean) bean);
        } else if (!StringUtils.isEmpty(beanDefinition.destroyMethodName())) {
            singletonBeanRegistry.registerDisposableBean(beanDefinition.beanName(), () -> {
                try {
                    Method destroyMethod = beanDefinition.beanClass().getMethod(beanDefinition.destroyMethodName());
                    destroyMethod.setAccessible(true);
                    destroyMethod.invoke(bean);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new BeansException(String.format("Fail to destory {%s} bean", beanDefinition.beanName()), e);
                }
            });
        }
    }

    /**
     * Creates a new instance of a bean using its constructor.
     */
    private Object createBeanInstance(BeanDefinition beanDefinition) {
        if (beanDefinition.factoryDefinition() != null) {
            return createBeanInstanceByFactory(beanDefinition);
        }

        return createBeanInstanceByConstructor(beanDefinition);

    }

    private Object createBeanInstanceByFactory(BeanDefinition beanDefinition) {
        FactoryDefinition factpryDefinition = beanDefinition.factoryDefinition();

        String factoryName = factpryDefinition.factoryName();
        Object factory = getBean(factoryName);
        String factoryMethodName = factpryDefinition.factoryMethodName();

        Class<?>[] parameterTypes = factpryDefinition.factoryMethodParamTypes();
        try {
            Method factoryMethod = factory.getClass().getMethod(factoryMethodName, parameterTypes);
            Object[] arguBeans = createArguBeans(parameterTypes);
            factoryMethod.setAccessible(true);
            return arguBeans == null ? factoryMethod.invoke(factory) : factoryMethod.invoke(factory, arguBeans);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            throw new BeansException(
                    String.format("Bean {%s} can't be created by factory method {%s} in factory {%s}",
                            beanDefinition.beanName(), factoryMethodName, factoryName));
        }
    }

    private Object createBeanInstanceByConstructor(BeanDefinition beanDefinition) {
        Constructor<?> constructorToUse = beanDefinition.constructor();
        Class<?> beanClass = beanDefinition.beanClass();
        Class<?>[] parameterTypes = null;
        if (constructorToUse != null) {
            parameterTypes = constructorToUse.getParameterTypes();
        }
        return instantiationStrategy.instance(beanClass, constructorToUse, createArguBeans(parameterTypes));
    }

    private Object[] createArguBeans(Class<?>[] parameterTypes) {
        if (ClassUtils.isArrayEmpty(parameterTypes)) {
            return null;
        }

        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < args.length; i++) {
            Map<String, ?> beansOfType = getBeansOfType(parameterTypes[i]);
            if (beansOfType.size() == 1) {
                args[i] = beansOfType.values().iterator().next();
            } else {
                throw new NoSuchBeanException(parameterTypes[i]);
            }
        }
        return args;
    }

    /**
     * Populates bean properties from its property values.
     */
    private void fillFieldValues(BeanDefinition beanDefinition, Object bean) {
        FieldValueList fieldValueList = beanDefinition.fieldValueList();
        if (fieldValueList == null) {
            return;
        }
        Class<?> beanClass = beanDefinition.beanClass();
        for (FieldValue fieldValue : fieldValueList) {
            String propertyName = fieldValue.name();
            Object value = fieldValue.value();

            if (value instanceof BeanReference) {
                BeanReference beanReference = (BeanReference) value;
                value = generalBeanFactory.getBean(beanReference.beanName());
            }

            try {
                Field declaredField = beanClass.getDeclaredField(propertyName);
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                throw new BeansException("Error setting property values: " + beanClass.getName(), e);
            }
        }
    }

    /**
     * Initializes the bean by invoking aware methods, initialization callbacks,
     * and applying post processors.
     */
    private Object initializeBean(Object bean, BeanDefinition beanDefinition) {
        fillFieldValues(beanDefinition, bean);

        methodInject(bean, beanDefinition);

        String beanName = beanDefinition.beanName();

        awareBean(beanName, bean);

        beanProcessors.stream().filter(beanProcessor -> {
            return (beanProcessor instanceof BeforeInitializationProcessor);
        })
                .forEach(beanPostProcessor -> ((BeforeInitializationProcessor) beanPostProcessor)
                        .postProcessBeforeInitialization(bean, beanName));

        invokeInitMethods(bean, beanDefinition);

        beanProcessors.stream().filter(beanProcessor -> {
            return (beanProcessor instanceof AfterInitializationpProcessor);
        })
                .forEach(beanPostProcessor -> ((AfterInitializationpProcessor) beanPostProcessor)
                        .postProcessAfterInitialization(bean, beanName));
        return bean;
    }

    /**
     * Invokes aware interface methods if the bean implements any Aware interfaces.
     */
    private void awareBean(String beanName, Object bean) {
        if (!(bean instanceof Aware)) {
            return;
        }

        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }

        if (bean instanceof BeanClassLoaderAware) {
            ((BeanClassLoaderAware) bean).setBeanClassLoader(ClassUtils.getDefaultClassLoader());
        }
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }
    }

    /**
     * Performs method injection for methods annotated with @AutoWired.
     * This method processes methods with a single parameter that should be
     * autowired with a bean from the container.
     *
     * @param wrappedBean    the bean instance being configured
     * @param beanDefinition the bean definition
     * @throws BeansException if method injection fails
     */
    private void methodInject(Object wrappedBean, BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.beanClass();
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            AutoWired autoWired = method.getAnnotation(AutoWired.class);
            if (autoWired == null) {
                continue;
            }
            int count = method.getParameterCount();

            if (count == 0) {
                throw new BeansException(String.format("Class {%s}'s inject method {%s} has no arugment",
                        beanClass, method.getName()));
            }

            if (count > 1) {
                throw new BeansException(String.format("Class {%s}'s inject method {%s} has no arugment",
                        beanClass, method.getName()));
            }

            Class<?> parameterType = method.getParameterTypes()[0];

            setInjectingParameter(wrappedBean, method, parameterType);

        }
    }

    private void setInjectingParameter(Object wrappedBean, Method method, Class<?> parameterType) {
        if (parameterType.isPrimitive()) {
            throw new BeansException(String.format("Class {%s}'s inject method {%s} has primitive type arugment",
                    parameterType, method.getName()));
        }

        Object injectingBean = null;
        List<BeanDefinition> beanDefinitions = getBeanDefinitionsOfType(parameterType);
        if (ClassUtils.isCollectionEmpty(beanDefinitions)) {
            throw new NoSuchBeanException(parameterType);
        } else if (beanDefinitions.size() > 1) {
            throw new BeansException(String.format("Can not specify arugment bean {%s} in inject method {%s} ",
                    parameterType, method.getName()));

        } else {
            injectingBean = getBean(beanDefinitions.get(0).beanName());
        }

        try {
            method.invoke(wrappedBean, injectingBean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new BeansException(String.format("Class {%s}'s inject method {%s} failed",
                    parameterType, method.getName()), e);
        }

    }

    /**
     * Invokes initialization methods on the bean if it implements InitializingBean
     * or has a custom init method specified.
     *
     * @param beanName       the name of the bean
     * @param wrappedBean    the bean instance
     * @param beanDefinition the bean definition
     */
    private void invokeInitMethods(Object wrappedBean, BeanDefinition beanDefinition) {
        if (wrappedBean instanceof InitializingBean) {
            try {
                ((InitializingBean) wrappedBean).afterPropertiesSet();
            } catch (Exception e) {
                throw new BeansException(String.format("{%s} inits in failure", beanDefinition.beanName()));
            }
        }

        String initMethodName = beanDefinition.initMethodName();
        if (StringUtils.isEmpty(initMethodName)) {
            return;
        }

        try {
            Method initMethod = beanDefinition.beanClass().getMethod(initMethodName);
            initMethod.setAccessible(true);
            initMethod.invoke(wrappedBean);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | InvocationTargetException e) {
            throw new BeansException(
                    String.format("{%s} inits by {%s} in failure", beanDefinition.beanName(), initMethodName));
        }

    }
}