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
package com.pythongong.beans.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.FactoryBean;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.exception.BeansException;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;

/**
 * Support base class for bean factories that need to handle FactoryBean instances.
 * Provides caching capabilities for FactoryBean objects to improve performance
 * and ensure consistency in singleton scope.
 *
 * @author Cheng Gong
 */
public class FactoryBeanRegistrySupport {

    /** Cache of singleton objects created by FactoryBeans */
    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    /**
     * Returns the cached object for a FactoryBean.
     *
     * @param beanName the name of the bean to retrieve from cache
     * @return the cached object, or null if not found
     */
    protected Object getCachedObjectForFactoryBean(String beanName) {
        CheckUtils.emptyString(beanName, "FactoryBeanRegistrySupport.getCachedObjectForFactoryBean recevies empty bean name");
        Object cachedObject = factoryBeanObjectCache.get(beanName);
        return cachedObject == ClassUtils.NULL_OBJECT ? null : cachedObject;
    }

    /**
     * Obtains an object from the FactoryBean.
     * For singleton-scoped factory beans, the object will be cached.
     *
     * @param factory the FactoryBean instance
     * @param beanDefinition the bean definition for the factory bean
     * @return the object obtained from the factory
     * @throws BeansException if the factory bean throws an exception
     */
    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, BeanDefinition beanDefinition) {
        CheckUtils.nullArgs(beanDefinition, "FactoryBeanRegistrySupport.getObjectFromFactoryBean recevies null bean definition");
        CheckUtils.nullArgs(factory, "FactoryBeanRegistrySupport.getObjectFromFactoryBean recevies null factory bean");

        Object curObject = null;
        String beanName = beanDefinition.beanName();
        try {
            curObject = factory.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean throws exception on object: " + beanName);
        }

        // For non-singleton scoped beans, return without caching
        if (!ScopeEnum.SINGLETON.equals(beanDefinition.scope())) {
            return curObject;
        }

        // Check if object is already cached
        Object cachedObject = factoryBeanObjectCache.get(beanName);
        if (cachedObject != null) {
            return cachedObject == ClassUtils.NULL_OBJECT ? null : cachedObject;
        }

        // Cache the object (use NULL_OBJECT as placeholder for null values)
        factoryBeanObjectCache.put(beanName, curObject == null ? ClassUtils.NULL_OBJECT : curObject);
        return curObject;
    }
}
