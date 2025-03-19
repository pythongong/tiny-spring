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
package com.pythongong.beans.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.aware.Aware;
import com.pythongong.beans.aware.BeanClassLoaderAware;
import com.pythongong.beans.aware.BeanFactoryAware;
import com.pythongong.beans.aware.BeanNameAware;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.config.BeanReference;
import com.pythongong.beans.config.DisposableBean;
import com.pythongong.beans.config.InitializingBean;
import com.pythongong.beans.config.FieldValue;
import com.pythongong.beans.config.FieldValueList;
import com.pythongong.beans.factory.AutowireCapableBeanFactory;
import com.pythongong.beans.factory.ConfigurableListableBeanFactory;
import com.pythongong.beans.registry.BeanDefinitionRegistry;
import com.pythongong.context.event.ApplicationEventMulticaster;
import com.pythongong.context.event.GeneralApplicationEventMulticaster;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.exception.BeansException;
import com.pythongong.exception.NoSuchBeanException;
import com.pythongong.stereotype.AutoWired;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;

/**
 * Default implementation of the ConfigurableListableBeanFactory and BeanDefinitionRegistry interfaces.
 * This is a complete bean factory implementation that supports singleton and prototype beans,
 * Aware interfaces, lifecycle methods, property injection, and bean post processing.
 *
 * @author Cheng Gong
 */
public class DefaultListableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory, AutowireCapableBeanFactory {

    /** Map of bean definitions, keyed by bean name */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

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

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        return beanDefinitionMap.get(beanName);
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        CheckUtils.nullArgs(beanDefinition, "DefaultListableBeanFactory.registerBeanDefinition recevies null bean definition");
        CheckUtils.emptyString(beanDefinition.beanName(), "DefaultListableBeanFactory.registerBeanDefinition recevies empty bean name");
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

        if (results.isEmpty()) {
            throw new NoSuchBeanException(type);
        }

        return results;
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return this.generalBeanFactory.getBean(name);
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonBeanRegistry.getSingleton(beanName);
    }

    @Override
    public void destroySingletons() {
        singletonBeanRegistry.destroySingletons();
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        generalBeanFactory.addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return generalBeanFactory.getBean(name, requiredType);
    }

    @Override
    public ApplicationEventMulticaster initApplicationEventMulticaster() {
        GeneralApplicationEventMulticaster applicationEventMulticaster = new GeneralApplicationEventMulticaster();
        singletonBeanRegistry.registerSingleton(ClassUtils.APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
        return applicationEventMulticaster;
    }

    /**
     * Applies BeanPostProcessors to the given bean instance before initialization.
     *
     * @param existingBean the existing bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
         Object result = existingBean;
         for (BeanPostProcessor processor : generalBeanFactory.getBeanPostProcessors()) {
             Object current = processor.postProcessBeforeInitialization(result, beanName);
             if (current == null) {
                return result;
             }
             result = current;
         }
         return result;
    }

    /**
     * Applies BeanPostProcessors to the given bean instance after initialization.
     *
     * @param existingBean the existing bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one
     */
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : generalBeanFactory.getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }

    /**
     * Creates a new instance of a bean from its definition.
     *
     * @param beanDefinition the bean definition to create an instance from
     * @return the created bean instance
     */
    protected Object createBean(BeanDefinition beanDefinition) throws BeansException {
        Object bean;
        String beanName = beanDefinition.beanName();
        
        bean = createBeanInstance(beanDefinition);

        if (ScopeEnum.SINGLETON.equals(beanDefinition.scope())) {
            singletonBeanRegistry.registerSingleton(beanName, bean);
        }
        
        fillFieldValues(beanDefinition, bean);

        bean = initializeBean(bean, beanDefinition);

        registerDisposableBeanIfNecessary(bean, beanDefinition);

        if (ScopeEnum.SINGLETON.equals(beanDefinition.scope())) {
            singletonBeanRegistry.registerSingleton(beanName, bean);
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
        if (bean instanceof DisposableBean || beanDefinition.destroyMethod() != null) {
            singletonBeanRegistry.registerDisposableBean(
                beanDefinition.beanName(), 
                new DisposableBeanAdapter(bean, beanDefinition.destroyMethod())
            );
        }
    }

    /**
     * Creates a new instance of a bean using its constructor.
     */
    private Object createBeanInstance(BeanDefinition beanDefinition) {
        Constructor<?> constructorToUse = beanDefinition.constructor();
        Class<?> beanClass = beanDefinition.beanClass();
        if (constructorToUse == null) {
            return instantiationStrategy.instance(beanClass, constructorToUse, null);
        }
        Class<?>[] parameterTypes = constructorToUse.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = getBeansOfType(parameterTypes[i]);
        }
        return instantiationStrategy.instance(beanClass, constructorToUse, args);
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
                value = generalBeanFactory.getBean(beanReference.getBeanName());
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
        String beanName = beanDefinition.beanName();
        awareBean(beanName, bean);

        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("DefaultListableBeanFactory.invokeInitMethods failed", e);
        }

        methodInject(beanName, wrappedBean, beanDefinition);

        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return wrappedBean;
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
        
        if (bean instanceof BeanClassLoaderAware){
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
     * @param beanName the name of the bean being processed
     * @param wrappedBean the bean instance being configured
     * @param beanDefinition the bean definition
     * @throws BeansException if method injection fails
     */
    private void methodInject(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getClass();
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

            Object bean = generalBeanFactory.getBean(beanName);
            
            try {
                method.invoke(wrappedBean, bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BeansException(String.format("Class {%s}'s inject method {%s} failed", 
                    beanClass, method.getName()), e);
            }

            break;
        }
    }

    /**
     * Invokes initialization methods on the bean if it implements InitializingBean
     * or has a custom init method specified.
     *
     * @param beanName the name of the bean
     * @param wrappedBean the bean instance
     * @param beanDefinition the bean definition
     * @throws Exception if initialization fails
     */
    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) 
            throws Exception {
        if (wrappedBean instanceof InitializingBean) {
            ((InitializingBean) wrappedBean).afterPropertiesSet();
            return;
        }

        Method initMethod = beanDefinition.initMethod();
        if (initMethod == null) {
            return;
        }

        initMethod.invoke(wrappedBean);
    }
}