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
package com.pythongong.exception;

/**
 * Exception thrown when a bean cannot be found in the container.
 * This can occur when attempting to retrieve a bean by name or type
 * that does not exist in the container.
 *
 * @author pythongong
 * @since 2025-03-18
 */
public class NoSuchBeanException extends BeansException {

    /**
     * Constructs a new NoScuhBeanException with the specified message.
     *
     * @param msg the detail message
     */
    public NoSuchBeanException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new NoScuhBeanException for a bean not found by name and type.
     *
     * @param beanName the name of the bean that was not found
     * @param beanClass the expected type of the bean
     */
    public NoSuchBeanException(Object beanName, Class<?> beanClass) {
        this(String.format("No bean named: {%s}, type: {%s}", beanName, beanClass.getName()));
    }
    
    /**
     * Constructs a new NoScuhBeanException for a bean not found by type.
     *
     * @param beanType the type of the bean that was not found
     */
    public NoSuchBeanException(Class<?> beanType) {
        this(String.format("No bean as type: {%s}", beanType.getName()));
    }
}
