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

import java.util.Map;
import com.pythongong.exception.BeansException;

/**
 * Extension of the {@link BeanFactory} interface to be implemented by bean factories
 * that can enumerate all their bean instances, rather than just accessing bean
 * instances by name on demand.
 *
 * @author Cheng Gong
 */
public interface ListableBeanFactory extends BeanFactory {

    /**
     * Returns a map of all beans of the given type in the factory.
     *
     * @param <T> the required type of the beans
     * @param type the class or interface to match
     * @return a Map of bean names and their corresponding bean instances
     * @throws BeansException if the beans could not be created
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
}
