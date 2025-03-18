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

/**
 * Interface that defines a registry for singleton beans.
 * Used to track and manage singleton instances in a Spring container.
 *
 * @author Cheng Gong
 * @since 2025-03-18
 */
public interface SingletonBeanRegistry {
    
    /**
     * Returns the singleton bean instance registered under the given name.
     *
     * @param beanName the name of the bean to look for
     * @return the registered singleton instance, or null if none found
     */
    Object getSingleton(String beanName);

    /**
     * Destroys all singleton beans in this registry.
     * Typically called on shutdown of a bean factory.
     */
    void destroySingletons();
}
