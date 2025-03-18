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
package com.pythongong.beans.config;

/**
 * Interface to be implemented by objects used to create other objects.
 * This is an alternative approach of creating objects compared to regular
 * bean instantiation. While a regular bean is created directly by the container,
 * a FactoryBean implementation allows for custom object creation logic.
 *
 * @param <T> the type of object that this FactoryBean creates
 * @author Cheng Gong
 */
@FunctionalInterface
public interface FactoryBean<T> {

    /**
     * Returns an instance of the object this factory creates.
     *
     * @return the object instance
     * @throws Exception if an error occurs while creating the object
     */
    T getObject() throws Exception;
}
