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
package com.pythongong.util;

import com.pythongong.exception.BeansException;

/**
 * Utility class providing validation methods for common checks.
 * This class contains static methods for validating arguments and ensuring
 * preconditions are met.
 *
 * @author Cheng Gong
 */
public class CheckUtils {
    
    /** Private constructor to prevent instantiation */
    private CheckUtils() {}

    /**
     * Checks if an object is null and throws an BeansException if it is.
     *
     * @param obj the object to check
     * @param msg the error message to use if the object is null
     * @throws BeansException if the object is null
     */
    public static void nullArgs(Object obj, String msg) {
        if (obj == null) {
            throw new BeansException(msg);
        }
    }

    /**
     * Checks if an array is null or empty and throws an BeansException if it is.
     *
     * @param array the array to check
     * @param msg the error message to use if the array is null or empty
     * @throws BeansException if the array is null or empty
     */
    public static void emptyArray(Object[] array, String msg) {
        if (ClassUtils.isArrayEmpty(array)) {
            throw new BeansException(msg);
        }
    }

    /**
     * Checks if a string is null, empty, or contains only whitespace.
     *
     * @param str the string to check
     * @param msg the error message to use if the string is empty
     * @throws BeansException if the string is null, empty, or contains only whitespace
     */
    public static void emptyString(String str, String msg) {
        if (StringUtils.isEmpty(str)) {
            throw new BeansException(msg);
        }
    }
}
