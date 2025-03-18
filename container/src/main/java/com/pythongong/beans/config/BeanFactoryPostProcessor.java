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
package com.pythongong.beans.config;

import com.pythongong.beans.factory.ConfigurableListableBeanFactory;
import com.pythongong.exception.BeansException;

/**
 * Factory hook that allows for custom modification of a bean factory's bean definitions.
 * Useful for modifying bean definitions before any beans are instantiated.
 *
 * @author Cheng Gong
 */
public interface BeanFactoryPostProcessor {

    /**
     * Modifies the bean factory's internal bean definitions after they have all been
     * loaded but before any beans are instantiated. This allows for overriding or
     * adding properties even to eager-initializing beans.
     *
     * @param beanFactory the bean factory to process
     * @throws BeansException in case of errors
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
