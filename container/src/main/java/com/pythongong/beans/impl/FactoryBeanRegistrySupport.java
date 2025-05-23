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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.config.FactoryBean;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Nullable;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;

/**
 * Support base class for bean factories that need to handle FactoryBean
 * instances.
 * Provides caching capabilities for FactoryBean objects to improve performance
 * and ensure consistency in singleton scope.
 *
 * @author Cheng Gong
 */
public class FactoryBeanRegistrySupport {

    /** Cache of singleton objects created by FactoryBeans */
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>(ClassUtils.SMALL_INIT_SIZE);

    /**
     * Returns the cached object for a FactoryBean.
     *
     * @param beanName the name of the bean to retrieve from cache
     * @return the cached object, or null if not found
     */
    @Nullable
    Object getCachedObjectForFactoryBean(String beanName) {
        CheckUtils.emptyString(beanName,
                "FactoryBeanRegistrySupport.getCachedObjectForFactoryBean recevies empty bean name");
        Object cachedObject = factoryBeanObjectCache.get(beanName);
        return cachedObject;
    }

    /**
     * Obtains an object from the FactoryBean.
     * For singleton-scoped factory beans, the object will be cached.
     *
     * @param factory        the FactoryBean instance
     * @param beanDefinition the bean definition for the factory bean
     * @return the object obtained from the factory
     * @throws BeansException if the factory bean throws an exception
     */
    @Nullable
    Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName) {
        CheckUtils.nullArgs(factory, "FactoryBeanRegistrySupport.getObjectFromFactoryBean recevies null factory bean");
        Object curObject = null;
        try {
            curObject = factory.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean throws exception on object: " + beanName);
        }

        // For non-singleton scoped beans, return without caching
        if (!factory.isSingleton()) {
            return curObject;
        }

        CheckUtils.emptyString(beanName,
                "FactoryBeanRegistrySupport.getObjectFromFactoryBean recevies empty bean name");
        // Check if object is already cached
        Object cachedObject = factoryBeanObjectCache.get(beanName);
        if (cachedObject != null) {
            return cachedObject;
        }

        if (curObject == null) {
            throw new BeansException("FactoryBean returned null object: " + beanName);
        }
        factoryBeanObjectCache.put(beanName, curObject);
        return curObject;
    }
}
