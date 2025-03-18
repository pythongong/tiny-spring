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
package com.pythongong.beans;

import com.pythongong.beans.config.BeanDefinition;

/**
 * Interface for registries that hold bean definitions.
 * Provides the ability to register bean definitions in a Spring container.
 *
 * @author Cheng Gong
 */
public interface BeanDefinitionRegistry {

    /**
     * Registers a new bean definition in this registry.
     *
     * @param beanDefinition the bean definition to register
     */
    void registerBeanDefinition(BeanDefinition beanDefinition);
} 
