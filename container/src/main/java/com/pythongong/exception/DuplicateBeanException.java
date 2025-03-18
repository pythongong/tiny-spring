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
 * Exception thrown when attempting to register a bean with a name
 * that is already in use within the container.
 *
 * @author pythongong
 * @since 2025-03-18
 */
public class DuplicateBeanException extends BeansException {

    /**
     * Constructs a new DuplicateBeanExcpetion with the specified message.
     *
     * @param msg the detail message
     */
    public DuplicateBeanException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs a new DuplicateBeanExcpetion with details about the duplicate bean.
     *
     * @param beanName the name of the bean that was duplicated
     * @param beanClass the type of the bean that was duplicated
     */
    public DuplicateBeanException(String beanName, Class<?> beanClass) {
        this(String.format("Duplicate bean named: {%s}, type: {%s}", beanName, beanClass.getName()));
    }
}
