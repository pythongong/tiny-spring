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
package com.pythongong.beans.registry;

import com.pythongong.beans.config.BeanDefinition;

/**
 * Interface for registries that hold bean definitions.
 * <p>This is the central interface for registering bean definitions
 * in a Spring container. Typically implemented by bean factories that
 * store bean definitions internally.
 * 
 * <p>The main purpose is to allow tools and other extensions to 
 * register bean definitions in a consistent way.
 *
 * @author pythongong
 * @since 2025-03-18 03:22:24
 */
public interface BeanDefinitionRegistry {

    /**
     * Register a new bean definition with this registry.
     * <p>Must support repeated registration of the same name with
     * different definitions, i.e. an override of the previous definition.
     *
     * @param beanDefinition definition of the bean instance to register
     * @throws IllegalArgumentException if the bean definition is invalid
     */
    void registerBeanDefinition(BeanDefinition beanDefinition);
}