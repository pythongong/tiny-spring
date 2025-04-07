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

import com.pythongong.beans.config.AfterInitializationpProcessor;
import com.pythongong.beans.config.BeforeInitializationProcessor;
import com.pythongong.context.ApplicationContext;
import com.pythongong.context.ApplicationContextAware;
import com.pythongong.exception.BeansException;

/**
 * A bean post-processor that automatically injects the ApplicationContext into
 * beans that implement the ApplicationContextAware interface. This processor
 * enables beans to access the application context programmatically during
 * their initialization phase.
 *
 * @author Cheng Gong
 */
public class ApplicationContextAwareProcessor implements BeforeInitializationProcessor, AfterInitializationpProcessor {

    /**
     * The ApplicationContext instance to be injected into aware beans
     */
    private final ApplicationContext applicationContext;

    /**
     * Creates a new ApplicationContextAwareProcessor with the specified
     * ApplicationContext
     *
     * @param applicationContext the ApplicationContext to be injected into aware
     *                           beans
     */
    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    /**
     * Injects the ApplicationContext into beans that implement
     * ApplicationContextAware
     * before their initialization.
     *
     * @param bean     the bean instance being processed
     * @param beanName the name of the bean
     * @throws BeansException if any error occurs during processing
     */
    @Override
    public void postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationContextAware) {
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }
    }

    /**
     * No post-processing is performed after initialization
     *
     * @param bean     the bean instance being processed
     * @param beanName the name of the bean
     * 
     * @throws BeansException if any error occurs during processing
     */
    @Override
    public void postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

    }
}