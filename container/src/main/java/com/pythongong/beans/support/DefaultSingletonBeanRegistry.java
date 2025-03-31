
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.config.DisposableBean;
import com.pythongong.beans.registry.SingletonBeanRegistry;
import com.pythongong.exception.BeansException;
import com.pythongong.exception.NoSuchBeanException;
import com.pythongong.util.CheckUtils;

/**
 * Default implementation of the SingletonBeanRegistry interface, providing a
 * central registry for shared bean instances.
 * <p>
 * Supports registration of disposable beans, which will be destroyed when
 * the registry is destroyed. Uses thread-safe collections to support concurrent
 * access to singleton beans.
 *
 * @author Cheng Gong
 * @see SingletonBeanRegistry
 * @see DisposableBean
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /** Cache of singleton objects: bean name to bean instance */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /** Map of disposable beans: bean name to disposable instance */
    private final Map<String, DisposableBean> disposableBeanMap = new HashMap<>();

    /**
     * Register a disposable bean for later destruction.
     *
     * @param beanName the name of the bean
     * @param bean     the disposable bean instance
     * @throws IllegalArgumentException if beanName is empty or bean is null
     */
    public void registerDisposableBean(String beanName, DisposableBean bean) {
        CheckUtils.emptyString(beanName,
                "DefaultSingletonBeanRegistry.registerDisposableBean receives empty bean name");
        CheckUtils.nullArgs(bean, "DefaultSingletonBeanRegistry.registerDisposableBean receives null bean");
        disposableBeanMap.put(beanName, bean);
    }

    @Override
    public Object getSingleton(String beanName) {
        CheckUtils.emptyString(beanName, "DefaultSingletonBeanRegistry.getSingleton receives empty bean name");
        return singletonObjects.get(beanName);
    }

    @Override
    public void destroySingletons() {
        Object[] disaposableNames = disposableBeanMap.keySet().toArray();

        Arrays.stream(disaposableNames).forEach(beanName -> {
            DisposableBean bean = disposableBeanMap.get(beanName);
            if (bean == null) {
                throw new NoSuchBeanException((String) beanName, DisposableBean.class);
            }
            disposableBeanMap.remove(beanName);
            try {
                bean.destroy();
            } catch (Exception e) {
                throw new BeansException(String.format("Fail to destory {%s} bean", beanName), e);
            }
        });
    }

    /**
     * Register the given existing object as singleton in the bean registry.
     *
     * @param beanName the name of the bean
     * @param bean     the bean instance
     * @throws IllegalArgumentException if beanName is empty or bean is null
     */
    void registerSingleton(String beanName, Object bean) {
        CheckUtils.emptyString(beanName, "DefaultSingletonBeanRegistry.registerSingleton receives empty bean name");
        CheckUtils.nullArgs(bean, "DefaultSingletonBeanRegistry.registerSingleton receives null bean");
        singletonObjects.put(beanName, bean);
    }

}