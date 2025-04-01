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
package com.pythongong.context.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Properties;

import com.pythongong.exception.NoSuchBeanException;
import com.pythongong.stereotype.Nullable;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.StringUtils;

/**
 * A property resolver that handles property placeholder resolution and provides
 * access to configuration properties. This resolver supports both system
 * environment
 * variables and custom properties loaded from property files. It also supports
 * default values in property placeholders using the syntax ${key:defaultValue}.
 *
 * @author Cheng Gong
 */
public class PropertyResolver {

    /**
     * The starting delimiter for property placeholders
     */
    private final static String START = "${";

    /**
     * The ending delimiter for property placeholders
     */
    private final static String END = "}";

    /**
     * The properties container holding all resolved properties and system
     * environment variables
     */
    private Properties properties;

    /**
     * Creates a new PropertyResolver with an empty properties container
     */
    public PropertyResolver() {
        this(null);
    }

    /**
     * Creates a new PropertyResolver with the specified properties
     * System environment variables are automatically added to the properties
     *
     * @param properties initial properties to use, may be null
     */
    public PropertyResolver(Properties properties) {
        this.properties = properties == null ? new Properties() : properties;
        this.properties.putAll(System.getenv());
    }

    public void addAll(Map<String, Object> propertyMap) {
        CheckUtils.emptyMap(propertyMap, "PropertyResolver.addAll", "propertyMap");
        properties.putAll(propertyMap);
    }

    /**
     * Loads properties from an input stream into this resolver
     *
     * @param inputStream the input stream containing properties to load
     * @throws IOException if an error occurs while loading the properties
     */
    public void load(InputStream inputStream) throws IOException {
        CheckUtils.nullArgs(inputStream, "PropertyResolver.load recevies null InputStream");
        try (inputStream) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IOException("Error loading properties", e);
        }
    }

    /**
     * Resolves a property value by its key. If the key is in the format
     * ${key:defaultValue},
     * returns the default value if the key doesn't exist.
     *
     * @param key the property key to resolve
     * @return the resolved property value
     * @throws NoSuchBeanException the property doesn't exist and no default value
     *                             is specified
     */
    public String getProperty(String key) {
        CheckUtils.emptyString(key, "PropertyResolver.getProperty receives empty key");
        PropertyExpr propertyExpr = parsePropertyExpr(key);
        String value = null;
        if (propertyExpr != null && propertyExpr.defaultValue() != null) {
            value = properties.getProperty(key, propertyExpr.defaultValue());
        } else if (propertyExpr != null) {
            value = properties.getProperty(propertyExpr.key());

        } else {
            value = properties.getProperty(key);
        }

        if (value == null) {
            throw new NoSuchElementException(key + " doesn't exist");
        }

        return value;
    }

    @Nullable
    public Object getProperty(String key, Class<?> targetType) {
        String value = getProperty(key);
        if (value == null) {
            return null;
        }
        if (targetType == String.class) {
            return value;
        }
        return StringUtils.convertString(value, targetType);
    }

    /**
     * Parses a property expression in the format ${key:defaultValue} or ${key}
     * into its components
     *
     * @param key the property expression to parse
     * @return a PropertyExpr object containing the parsed key and default value, or
     *         null if not a valid expression
     */
    private PropertyExpr parsePropertyExpr(String key) {
        if (!key.startsWith(START) || !key.endsWith(END)) {
            return null;
        }
        int defaultValueIndex = key.indexOf(":");
        if (defaultValueIndex == -1) {
            key = key.substring(START.length(), key.length() - END.length());
            return new PropertyExpr(key, null);
        }

        String defaultValue = key.substring(defaultValueIndex + 1, key.length() - END.length());
        key = key.substring(START.length(), defaultValueIndex);
        return new PropertyExpr(key, defaultValue);
    }
}

/**
 * Record class representing a parsed property expression containing a key
 * and an optional default value.
 */
record PropertyExpr(
        /**
         * The property key without the ${} delimiters
         */
        String key,

        /**
         * The default value to use if the property is not found,
         * or null if no default value was specified
         */
        String defaultValue) {
}