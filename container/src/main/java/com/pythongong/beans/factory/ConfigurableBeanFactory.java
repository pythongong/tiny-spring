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

import com.pythongong.beans.config.BeanProcessor;

/**
 * Configuration interface to be implemented by most bean factories.
 * <p>
 * Provides facilities to configure a bean factory beyond the bean
 * factory client methods in the {@link BeanFactory} interface.
 * 
 * <p>
 * This interface allows for framework-level customization of bean
 * creation and initialization processes through beanProcessors.
 *
 * @author Cheng Gong
 * @see BeanFactory
 * @see beanProcessor
 */
public interface ConfigurableBeanFactory extends BeanFactory {

    /**
     * Add a new beanProcessor that will get applied to beans created
     * by this factory. Post processors can perform custom modification
     * of bean instances, before and after initialization.
     *
     * <p>
     * The post processors will be applied in the order they were registered.
     *
     * @param beanProcessor the post processor to register
     */
    void addBeanProcessor(BeanProcessor beanProcessor);
}