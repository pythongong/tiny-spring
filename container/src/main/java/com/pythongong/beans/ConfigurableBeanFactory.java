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

import com.pythongong.beans.config.BeanPostProcessor;

/**
 * Configuration interface to be implemented by most bean factories.
 * Provides facilities to configure a bean factory in addition to the bean
 * factory client methods in the {@link BeanFactory} interface.
 *
 * @author Cheng Gong
 */
public interface ConfigurableBeanFactory extends BeanFactory {
    
    /**
     * Add a new BeanPostProcessor that will get applied to beans created
     * by this factory.
     *
     * @param beanPostProcessor the post processor to register
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
