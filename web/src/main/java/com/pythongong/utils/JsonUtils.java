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

package com.pythongong.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.pythongong.exception.WebException;
import com.pythongong.util.CheckUtils;

/**
 * Utility class for JSON serialization and deserialization.
 * 
 * <p>
 * Provides a preconfigured ObjectMapper and convenience methods
 * for reading and writing JSON data.
 *
 * @author pythongong
 * @since 1.0
 */
public class JsonUtils {

    /**
     * Private constructor to prevent instantiation.
     */
    private JsonUtils() {
    }

    /** Shared ObjectMapper instance with default configuration */
    public static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    static {
        DEFAULT_MAPPER.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        DEFAULT_MAPPER.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        DEFAULT_MAPPER.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        DEFAULT_MAPPER.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    /**
     * Writes an object as JSON to the specified writer.
     * 
     * @param writer the writer to write JSON to
     * @param obj    the object to serialize
     * @throws WebException if serialization fails
     */
    public static void writeJson(PrintWriter writer, Object obj) {
        CheckUtils.nullArgs(writer, "JsonUtils.writeJson", "writer");
        CheckUtils.nullArgs(obj, "JsonUtils.writeJson", "obj");
        try {
            DEFAULT_MAPPER.writeValue(writer, obj);
        } catch (IOException e) {
            throw new WebException("Writing JSON failed for " + obj.getClass().getCanonicalName());
        }
    }

    /**
     * Reads JSON from a reader and converts it to the specified type.
     * 
     * @param reader    the reader containing JSON data
     * @param classType the target class type
     * @return the deserialized object
     * @throws WebException if deserialization fails
     */
    public static Object readJson(BufferedReader reader, Class<?> classType) {
        CheckUtils.nullArgs(reader, "JsonUtils.readJson", "reader");
        CheckUtils.nullArgs(classType, "JsonUtils.readJson", "classType");
        try {
            return DEFAULT_MAPPER.readValue(reader, classType);
        } catch (IOException e) {
            throw new WebException("Reading JSON failed for " + classType.getCanonicalName());
        }
    }

    /**
     * Converts an object to its JSON representation as a byte array.
     * @param obj the object to serialize
     * @return the JSON byte array
     * @throws WebException if serialization fails
     */
    public static byte[] toJsonBytes(Object obj) {
        CheckUtils.nullArgs(obj, "JsonUtils.toJsonBytes", "obj");
        try {
            return DEFAULT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new WebException("JSON convertion failed for " + obj.getClass().getCanonicalName());
        }
    }
}
