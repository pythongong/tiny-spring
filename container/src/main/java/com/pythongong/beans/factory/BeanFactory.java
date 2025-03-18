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

import com.pythongong.exception.BeansException;

/**
 * The root interface for accessing a Spring bean container.
 * This is the most fundamental interface of the Spring bean container,
 * providing basic bean retrieval functionality.
 * 
 * <p>The basic client view of a bean container, allowing for
 * programmatic retrieval of bean instances. Implementations should
 * support the Singleton and Prototype design patterns.
 *
 * @author pythongong
 * @since 2025-03-18 03:19:13
 * @see com.pythongong.beans.factory.ListableBeanFactory
 * @see com.pythongong.beans.factory.ConfigurableBeanFactory
 */
public interface BeanFactory {

    /**
     * Returns an instance of the bean registered with the given name.
     * Depending on the bean definition, either a singleton or a newly created
     * instance will be returned.
     *
     * @param beanName name of the bean to retrieve
     * @return an instance of the bean
     * @throws BeansException if the bean could not be obtained
     */
    Object getBean(String beanName) throws BeansException;

    /**
     * Returns an instance of the bean registered with the given name and ensures
     * it matches the required type.
     *
     * @param <T> the required type of the bean
     * @param beanName name of the bean to retrieve
     * @param requiredType type the bean must match; can be an interface or superclass
     * @return an instance of the bean cast to the required type
     * @throws BeansException if the bean could not be obtained or is not of the required type
     */
    <T> T getBean(String beanName, Class<T> requiredType) throws BeansException;
}