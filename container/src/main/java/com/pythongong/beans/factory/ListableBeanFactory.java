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

import java.util.Map;
import com.pythongong.exception.BeansException;

/**
 * Extension of the {@link BeanFactory} interface that can enumerate all its bean instances.
 * <p>This interface provides the ability to get beans of a specific type across the entire
 * factory, rather than just by name. BeanFactories that can enumerate their beans should
 * implement this interface.
 *
 * <p>This interface is implemented by most bean container implementations. In particular,
 * both standalone and web-based application contexts implement this interface.
 *
 * @author pythongong
 * @since 2025-03-18 03:19:13
 * @see BeanFactory
 */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * Returns a map of all beans of the given type in the factory.
     * <p>Includes beans created from FactoryBeans, which means that FactoryBeans
     * will be initialized to determine their object type.
     *
     * @param <T> the required type of the beans
     * @param type the class or interface to match
     * @return a Map with the matching beans, containing the bean names as
     *         keys and the corresponding bean instances as values
     * @throws BeansException if a bean could not be created
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
}