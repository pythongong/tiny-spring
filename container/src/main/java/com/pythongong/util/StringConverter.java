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

package com.pythongong.util;

/**
 * Functional interface for converting string values to objects.
 * 
 * <p>Provides a simple conversion mechanism from string representations
 * to their corresponding object types.
 *
 * @author pythongong
 * @since 1.0
 */
@FunctionalInterface
public interface StringConverter {

    /**
     * Converts a string source to its corresponding object representation.
     * 
     * @param source the string to be converted
     * @return the converted object
     */
    Object convert(String source);
}