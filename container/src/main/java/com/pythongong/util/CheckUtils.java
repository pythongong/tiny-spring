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

import java.util.Collection;
import java.util.Map;

/**
 * Utility class providing validation methods for common checks.
 * This class contains static methods for validating arguments and ensuring
 * preconditions are met.
 *
 * @author Cheng Gong
 */
public class CheckUtils {

    /** Private constructor to prevent instantiation */
    private CheckUtils() {
    }

    /**
     * Checks if an object is null and throws an IllegalArgumentException if it is.
     *
     * @param obj the object to check
     * @param msg the error message to use if the object is null
     * @throws IllegalArgumentException if the object is null
     */
    public static void nullArgs(Object obj, String msg) {
        if (obj == null) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Checks if an object is null or empty and
     * throws an IllegalArgumentException if it is.
     *
     * @param map    the object to check
     * @param method methd name for the error message
     * @param argu   argument name for the error message
     * @throws IllegalArgumentException if the object is null or empty
     */
    public static void nullArgs(Object obj, String method, String argu) {
        nullArgs(obj, String.format("{%s} recevies null {%s}", method, argu));
    }

    /**
     * Checks if an array is null or empty and throws an IllegalArgumentException if
     * it is.
     *
     * @param array the array to check
     * @param msg   the error message to use if the array is null or empty
     * @throws IllegalArgumentException if the array is null or empty
     */
    public static void emptyArray(Object[] array, String msg) {
        if (ClassUtils.isArrayEmpty(array)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Checks if a string is null, empty, or contains only whitespace.
     *
     * @param str the string to check
     * @param msg the error message to use if the string is empty
     * @throws IllegalArgumentException if the string is null, empty, or contains
     *                                  only whitespace
     */
    public static void emptyString(String str, String msg) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Checks if a string is null or empty and throws
     * an IllegalArgumentException if it is.
     *
     * @param map    the string to check
     * @param method methd name for the error message
     * @param argu   argument name for the error message
     * @throws IllegalArgumentException if the string is null or empty
     */
    public static void emptyString(String str, String method, String argu) {
        emptyString(str, String.format("{%s} recevies empty {%s}", method, argu));
    }

    public static void emptyCollection(Collection<?> collection, String msg) {
        if (ClassUtils.isCollectionEmpty(collection)) {
            throw new IllegalArgumentException(msg);
        }
    }

    /**
     * Checks if a collection is null or empty and throws an
     * IllegalArgumentException if it is.
     *
     * @param collection the collection to check
     * @param method     methd name for the error message
     * @param argu       argument name for the error message
     * @throws IllegalArgumentException if the collection is null or empty
     */
    public static void emptyCollection(Collection<?> collection, String method, String argu) {
        emptyCollection(collection, String.format("{%s} recevies empty {%s}", method, argu));
    }

    /**
     * Checks if a map is null or empty and throws an IllegalArgumentException if it
     * is.
     *
     * @param map    the map to check
     * @param method methd name for the error message
     * @param argu   argument name for the error message
     * @throws IllegalArgumentException if the map is null or empty
     */
    public static void emptyMap(Map<?, ?> map, String method, String argu) {
        if (ClassUtils.isMapEmpty(map)) {
            throw new IllegalArgumentException(String.format("{%s} recevies empty {%s}", method, argu));
        }
    }
}
