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
import java.lang.reflect.InvocationTargetException;

import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Nullable;
import com.pythongong.util.CheckUtils;

/**
 * Simple implementation of the {@link InstantiationStrategy} interface.
 * <p>
 * Creates new bean instances through constructor reflection.
 * If no constructor is specified, uses the default constructor.
 *
 * @author pythongong
 * @since 2025-03-18 03:22:24
 */
public class SimpleInstantiation implements InstantiationStrategy {

    /**
     * Creates a new instance of the specified class using either the provided
     * constructor
     * or the default constructor if none is specified.
     *
     * @param clazz       the class to instantiate
     * @param constructor the constructor to use, may be null for default
     *                    constructor
     * @param args        the arguments to pass to the constructor, may be null
     * @return the new instance
     * @throws BeansException if instantiation fails
     */
    @Override
    public Object instance(Class<?> clazz, @Nullable Constructor<?> constructor, Object[] args) throws BeansException {
        CheckUtils.nullArgs(clazz, "SimpleInstantiation.instance recevies null bean class");
        try {
            constructor = constructor == null ? clazz.getDeclaredConstructor() : constructor;
            if (constructor == null) {
                throw new BeansException(String.format("Failed to find constructor for class {%s}", clazz.getName()));

            }
            return args == null ? constructor.newInstance() : constructor.newInstance(args);
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | SecurityException | NoSuchMethodException e) {
            throw new BeansException(String.format("Failed to instantiate class {%s}", clazz.getName()), e);
        }
    }
}