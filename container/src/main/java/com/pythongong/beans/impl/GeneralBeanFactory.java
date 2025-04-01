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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.config.FactoryBean;
import com.pythongong.beans.factory.ConfigurableBeanFactory;
import com.pythongong.beans.registry.SingletonBeanRegistry;
import com.pythongong.exception.BeansException;
import com.pythongong.exception.NoSuchBeanException;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;

/**
 * General implementation of the ConfigurableBeanFactory interface.
 * This class provides the core functionality for bean instantiation and
 * configuration,
 * supporting both singleton and prototype scopes, as well as factory beans.
 *
 * @author Cheng Gong
 */
@SuppressWarnings("unchecked")
public class GeneralBeanFactory implements ConfigurableBeanFactory {

    /** Function to retrieve bean definitions by name */
    private final Function<String, BeanDefinition> getBeanDefinition;

    /** Function to create bean instances from definitions */
    private final Function<BeanDefinition, Object> createBean;

    /** Registry for managing singleton beans */
    private final SingletonBeanRegistry singletonBeanRegistry;

    /** Support class for handling FactoryBean instances */
    private final FactoryBeanRegistrySupport beanRegistrySupport = new FactoryBeanRegistrySupport();

    /** List of bean post processors */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>(ClassUtils.SMALL_INIT_SIZE);

    /**
     * Constructs a new GeneralBeanFactory with the specified dependencies.
     *
     * @param getBeanDefinition     function to retrieve bean definitions
     * @param createBean            function to create bean instances
     * @param singletonBeanRegistry registry for managing singleton beans
     */
    public GeneralBeanFactory(Function<String, BeanDefinition> getBeanDefinition,
            Function<BeanDefinition, Object> createBean, SingletonBeanRegistry singletonBeanRegistry) {

        CheckUtils.nullArgs(singletonBeanRegistry, "GeneralBeanFactory recevies null SingletonBeanRegistry");
        CheckUtils.nullArgs(createBean, "GeneralBeanFactory recevies null createBean function");
        CheckUtils.nullArgs(getBeanDefinition, "GeneralBeanFactory recevies null getBeanDefinition function");

        this.getBeanDefinition = getBeanDefinition;
        this.createBean = createBean;
        this.singletonBeanRegistry = singletonBeanRegistry;
    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        CheckUtils.emptyString(beanName, "GeneralBeanFactory.getBean recevies empty bean name");
        return doGetBean(beanName);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        CheckUtils.nullArgs(beanPostProcessor, "GeneralBeanFactory.addBeanPostProcessor recevies null processor");
        if (!beanPostProcessors.contains(beanPostProcessor)) {
            beanPostProcessors.add(beanPostProcessor);
        }
    }

    /**
     * Returns the list of BeanPostProcessors that will be applied to beans
     * created by this factory.
     *
     * @return the list of BeanPostProcessors
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        CheckUtils.emptyString(name, "GeneralBeanFactory.getBean recevies empty bean name");
        CheckUtils.nullArgs(requiredType, "GeneralBeanFactory.getBean recevies null bean type");
        return doGetBean(name);
    }

    /**
     * Template method for retrieving a bean instance, handling both
     * regular beans and FactoryBean instances.
     *
     * @param <T>      the type of bean to return
     * @param beanName the name of the bean to retrieve
     * @return the bean instance
     * @throws BeansException if the bean cannot be created
     */
    private <T> T doGetBean(String beanName) {
        Object sharedInstance = singletonBeanRegistry.getSingleton(beanName);
        if (sharedInstance != null) {
            return (T) getObjectForBeanInstance(sharedInstance, beanName);
        }
        BeanDefinition beanDefinition = getBeanDefinition.apply(beanName);
        if (beanDefinition == null) {
            return null;
        }

        return (T) getObjectForBeanInstance(createBean.apply(beanDefinition), beanName);
    }

    /**
     * Gets the object from a FactoryBean instance.
     *
     * @param sharedInstance the FactoryBean instance
     * @param beanDefinition the bean definition
     * @return the object created by the FactoryBean
     */
    private Object getObjectForBeanInstance(Object sharedInstance, String beanName) {
        if (!(sharedInstance instanceof FactoryBean)) {
            return sharedInstance;

        }

        Object cachedObject = beanRegistrySupport.getCachedObjectForFactoryBean(beanName);
        if (cachedObject == null) {
            cachedObject = beanRegistrySupport.getObjectFromFactoryBean((FactoryBean<?>) sharedInstance,
                    beanName);
        }

        if (cachedObject == null) {
            throw new NoSuchBeanException(beanName, sharedInstance.getClass());
        }
        return cachedObject;
    }
}
