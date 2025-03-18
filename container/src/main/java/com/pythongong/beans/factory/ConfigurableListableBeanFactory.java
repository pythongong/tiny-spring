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
package com.pythongong.beans.factory;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.registry.SingletonBeanRegistry;
import com.pythongong.context.event.ApplicationEventMulticaster;
import com.pythongong.exception.BeansException;

/**
 * Configuration interface to be implemented by most listable bean factories.
 * In addition to {@link ConfigurableBeanFactory}, it provides facilities to
 * analyze and modify bean definitions, and to pre-instantiate singletons.
 *
 * @author Cheng Gong
 */
public interface ConfigurableListableBeanFactory extends SingletonBeanRegistry, 
        ConfigurableBeanFactory, ListableBeanFactory {

    /**
     * Ensure that all non-lazy-init singletons are instantiated, also considering
     * FactoryBeans. Typically invoked at the end of factory setup.
     *
     * @throws BeansException if one of the singleton beans could not be created
     */
    void preInstantiateSingletons() throws BeansException;

    /**
     * Initialize the application event multicaster for this context.
     *
     * @return the initialized ApplicationEventMulticaster
     */
    ApplicationEventMulticaster initApplicationEventMulticaster();

    /**
     * Return the bean definition for the given bean name.
     *
     * @param beanName name of the bean to find a definition for
     * @return the BeanDefinition for the given name
     * @throws BeansException if the bean definition could not be resolved
     */
    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
}
