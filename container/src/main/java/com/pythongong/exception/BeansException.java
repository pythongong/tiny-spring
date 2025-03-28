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
 * Base exception for all beans-related exceptions in the framework.
 * This runtime exception is thrown when problems occur during bean
 * creation, configuration, or other bean operations.
 *
 * @author pythongong
 * @since 2025-03-18
 */
public class BeansException extends RuntimeException {

    /**
     * Constructs a new BeansException with the specified message.
     *
     * @param msg the detail message
     */
    public BeansException(String msg) {
        super(msg);
    }

    /**
     * Constructs a new BeansException with the specified message and cause.
     *
     * @param msg the detail message
     * @param cause the cause of the exception
     */
    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}