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

import java.lang.reflect.Constructor;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Nullable;

/**
 * Strategy interface for instantiating beans in a Spring bean factory.
 * <p>
 * This interface is used by bean factories to delegate bean instantiation.
 * Different implementations may use different approaches to create bean
 * instances.
 *
 * @author pythongong
 * @since 2025-03-18 03:22:24
 */
public interface InstantiationStrategy {

    /**
     * Create a new instance of a bean using the specified constructor and
     * arguments.
     *
     * @param clazz       the class to instantiate
     * @param constructor the constructor to use, may be null for default
     *                    constructor
     * @param args        the arguments to pass to the constructor, may be null
     * @return the new instance
     * @throws BeansException if instantiation fails
     */
    Object instance(Class<?> clazz, Constructor<?> constructor, @Nullable Object[] args) throws BeansException;
}
