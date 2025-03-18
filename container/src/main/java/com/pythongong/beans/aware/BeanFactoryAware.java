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
package com.pythongong.beans.aware;

import com.pythongong.beans.factory.BeanFactory;
import com.pythongong.exception.BeansException;

/**
 * Interface to be implemented by beans that wish to be aware of their
 * owning {@link BeanFactory}. This allows beans to access their containing
 * bean factory programmatically.
 *
 * @author Cheng Gong
 */
@FunctionalInterface
public interface BeanFactoryAware extends Aware {

    /**
     * Callback that supplies the owning factory to a bean instance.
     *
     * @param beanFactory owning BeanFactory
     * @throws BeansException in case of initialization errors
     */
    void setBeanFactory(BeanFactory beanFactory) throws BeansException;
}
