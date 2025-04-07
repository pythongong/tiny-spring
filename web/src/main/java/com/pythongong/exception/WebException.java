/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pythongong.exception;

/**
 * Exception thrown when web-related operations fail.
 * 
 * <p>This runtime exception is used to wrap servlet-related exceptions
 * and other web processing errors into a Spring-specific unchecked exception.
 * It serves as a base class for more specific web exceptions.
 *
 * @author pythongong
 * @since 1.0
 */
public class WebException extends RuntimeException {

    /**
     * Constructs a new web exception with the specified detail message.
     *
     * @param msg the detail message
     */
    public WebException(String msg) {
        super(msg);
    }
}
