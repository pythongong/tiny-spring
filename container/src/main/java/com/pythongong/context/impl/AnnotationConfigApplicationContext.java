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
package com.pythongong.context.impl;

import java.util.Map;
import java.util.Set;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanFactoryPostProcessor;
import com.pythongong.beans.config.BeanProcessor;
import com.pythongong.beans.factory.ConfigurableListableBeanFactory;
import com.pythongong.beans.impl.DefaultListableBeanFactory;
import com.pythongong.context.ApplicationContext;
import com.pythongong.context.annotation.ConfigurableClassParser;
import com.pythongong.context.event.ApplicationEvent;
import com.pythongong.context.event.ApplicationEventMulticaster;
import com.pythongong.context.event.ApplicationListener;
import com.pythongong.context.event.ConextClosedEvent;
import com.pythongong.context.event.ContextRefreshedEvent;
import com.pythongong.exception.BeansException;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.ContextUtils;

/**
 * Central class for handling annotation-based configuration and bootstrapping
 * of the application context.
 * This implementation allows for registration and processing of annotated
 * classes as configuration sources.
 * It provides support for scanning packages, loading property files, and
 * managing the complete lifecycle
 * of spring beans including initialization, dependency injection, and
 * destruction.
 * 
 * @author Cheng Gong
 */
public class AnnotationConfigApplicationContext implements ApplicationContext {

    /**
     * The configuration class that serves as the entry point for component scanning
     * and bean definitions.
     */
    private final Class<?> configurationClass;

    private final PropertyResolver propertyResolver;

    /**
     * The core container that holds bean definitions and handles bean
     * instantiation.
     */
    private DefaultListableBeanFactory beanFactory;

    /**
     * Handles the publishing of application events to registered listeners.
     */
    private ApplicationEventMulticaster applicationEventMulticaster;

    /**
     * Creates a new AnnotationConfigApplicationContext with the specified
     * configuration class.
     * The context will be immediately refreshed upon construction.
     *
     * @param configurationClass the configuration class that defines the
     *                           application context
     */
    public AnnotationConfigApplicationContext(Class<?> configurationClass) {
        this(configurationClass, null);

    }

    public AnnotationConfigApplicationContext(Class<?> configurationClass, PropertyResolver propertyResolver) {
        CheckUtils.nullArgs(configurationClass, "AnnotationConfigApplicationContext receives null class");
        this.configurationClass = configurationClass;
        this.propertyResolver = propertyResolver == null ? ContextUtils.createPropertyResolver() : propertyResolver;
        refresh();
    }

    /**
     * Refreshes the application context, processing bean definitions and
     * initializing
     * the container. This method follows a strict initialization order to ensure
     * proper
     * setup of all framework components.
     *
     * @throws BeansException if an error occurs during bean processing
     */
    @Override
    public void refresh() throws BeansException {
        this.beanFactory = new DefaultListableBeanFactory();

        refreshBeanFactory();

        beanFactory.addBeanProcessor(new ApplicationContextAwareProcessor(this));

        invokeBeanFactoryPostProcessors(beanFactory);

        registerBeanProcessors(beanFactory);

        applicationEventMulticaster = beanFactory.initApplicationEventMulticaster();

        beanFactory.preInstantiateSingletons();

        registerListeners();

        publishEvent(new ContextRefreshedEvent(this));
    }

    /**
     * Registers a shutdown hook with the JVM runtime to ensure proper context
     * cleanup when the JVM terminates.
     */
    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    /**
     * Closes this application context, publishing a context closed event and
     * destroying all singleton beans.
     */
    @Override
    public void close() {
        publishEvent(new ConextClosedEvent(this));
        beanFactory.destroySingletons();
    }

    /**
     * Retrieves all beans of the specified type from the application context.
     *
     * @param <T>  the type of beans to retrieve
     * @param type the class type to match
     * @return a Map of bean names and their corresponding instances
     * @throws BeansException if an error occurs while retrieving beans
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return this.beanFactory.getBeansOfType(type);
    }

    /**
     * Retrieves a bean instance by name from the application context.
     *
     * @param name the name of the bean to retrieve
     * @return the bean instance
     * @throws BeansException if the bean cannot be found or created
     */
    @Override
    public Object getBean(String name) throws BeansException {
        return this.beanFactory.getBean(name);
    }

    /**
     * Registers all ApplicationListener beans found in the context with the
     * ApplicationEventMulticaster.
     */
    @SuppressWarnings("rawtypes")
    private void registerListeners() {
        Map<String, ApplicationListener> listenerMap = beanFactory.getBeansOfType(ApplicationListener.class);
        listenerMap.forEach((name, lister) -> {
            applicationEventMulticaster.addApplicationListener(lister);
        });
    }

    /**
     * Refreshes the internal bean factory by parsing the configuration class
     * and registering all discovered bean definitions.
     *
     * @throws BeansException if an error occurs during bean factory refresh
     */
    private void refreshBeanFactory() throws BeansException {
        ConfigurableClassParser parser = new ConfigurableClassParser(propertyResolver);
        Set<BeanDefinition> beanDefinitions = parser.parse(configurationClass);
        beanDefinitions.forEach(this.beanFactory::registerBeanDefinition);
    }

    /**
     * Retrieves a bean of the specified type from the application context.
     *
     * @param <T>          the type of bean to retrieve
     * @param name         the name of the bean to retrieve
     * @param requiredType the required type of bean
     * @return the bean instance
     * @throws BeansException if the bean cannot be found or created
     */
    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return beanFactory.getBean(name, requiredType);
    }

    /**
     * Publishes an application event to all registered listeners.
     *
     * @param event the event to publish
     */
    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    /**
     * Invokes all registered BeanFactoryPostProcessors in the context.
     *
     * @param beanFactory the bean factory to post-process
     */
    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {

        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory
                .getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    /**
     * Registers all BeanProcessor beans with the bean factory.
     *
     * @param beanFactory the bean factory to register processors with
     */
    private void registerBeanProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanProcessor> beanProcessorMap = beanFactory.getBeansOfType(BeanProcessor.class);
        if (ClassUtils.isMapEmpty(beanProcessorMap)) {
            return;
        }
        for (BeanProcessor beanProcessor : beanProcessorMap.values()) {
            beanFactory.addBeanProcessor(beanProcessor);
        }
    }
}
