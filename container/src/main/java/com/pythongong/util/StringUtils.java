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

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for string operations.
 * This class provides common string manipulation and validation methods
 * used throughout the framework.
 *
 * @author Cheng Gong
 */
public class StringUtils {

    /** A map to store converters */
    private static final Map<Class<?>, StringConverter> stringConverters = new HashMap<>();

    /**
     * A static block to initialize the string converters.
     * It registers converters for various primitive types and their corresponding
     * wrapper classes.
     */
    static {
        stringConverters.put(String.class, s -> s);
        stringConverters.put(boolean.class, s -> Boolean.parseBoolean(s));
        stringConverters.put(Boolean.class, s -> Boolean.valueOf(s));

        stringConverters.put(byte.class, s -> Byte.parseByte(s));
        stringConverters.put(Byte.class, s -> Byte.valueOf(s));

        stringConverters.put(short.class, s -> Short.parseShort(s));
        stringConverters.put(Short.class, s -> Short.valueOf(s));

        stringConverters.put(int.class, s -> Integer.parseInt(s));
        stringConverters.put(Integer.class, s -> Integer.valueOf(s));

        stringConverters.put(long.class, s -> Long.parseLong(s));
        stringConverters.put(Long.class, s -> Long.valueOf(s));

        stringConverters.put(float.class, s -> Float.parseFloat(s));
        stringConverters.put(Float.class, s -> Float.valueOf(s));

        stringConverters.put(double.class, s -> Double.parseDouble(s));
        stringConverters.put(Double.class, s -> Double.valueOf(s));

        stringConverters.put(LocalDate.class, s -> LocalDate.parse(s));
        stringConverters.put(LocalTime.class, s -> LocalTime.parse(s));
        stringConverters.put(LocalDateTime.class, s -> LocalDateTime.parse(s));
        stringConverters.put(ZonedDateTime.class, s -> ZonedDateTime.parse(s));
        stringConverters.put(Duration.class, s -> Duration.parse(s));
        stringConverters.put(ZoneId.class, s -> ZoneId.of(s));
    }

    /**
     * Converts a string to the specified target class.
     * 
     * @param source      the string to convert
     * @param targetClass the target class to convert to
     * @return the converted object
     */
    public static Object convertString(String source, Class<?> targetClass) {
        StringConverter stringConverter = stringConverters.get(targetClass);
        if (stringConverter == null) {
            throw new IllegalArgumentException();
        }
        return stringConverter.convert(source);
    }

    /** Private constructor to prevent instantiation */
    private StringUtils() {
    }

    /**
     * Checks if a string is null, empty, or contains only whitespace.
     *
     * @param str the string to check
     * @return true if the string is null, empty, or contains only whitespace;
     *         false otherwise
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isBlank();
    }
}
