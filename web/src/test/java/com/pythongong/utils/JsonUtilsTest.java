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

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;

import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link JsonUtils}.
 */
class JsonUtilsTest {

    @Test
    void testWriteAndReadJson() {
        // Test data
        TestObject original = new TestObject("test", 42);
        
        // Write to JSON
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        JsonUtils.writeJson(writer, original);
        
        // Read from JSON
        String json = stringWriter.toString();
        BufferedReader reader = new BufferedReader(new StringReader(json));
        TestObject result = (TestObject) JsonUtils.readJson(reader, TestObject.class);
        
        // Verify
        assertNotNull(result);
        assertEquals(original.name(), result.name());
        assertEquals(original.value(), result.value());
    }

    @Test
    void testWriteJsonWithNullValues() {
        TestObject original = new TestObject(null, 0);
        
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        JsonUtils.writeJson(writer, original);
        
        String json = stringWriter.toString();
        assertTrue(json.contains("null"));
    }

    @Test
    void testReadJsonWithUnknownProperties() {
        String json = "{\"name\":\"test\",\"value\":42,\"unknown\":\"extra\"}";
        BufferedReader reader = new BufferedReader(new StringReader(json));
        
        TestObject result = (TestObject) JsonUtils.readJson(reader, TestObject.class);
        
        assertNotNull(result);
        assertEquals("test", result.name());
        assertEquals(42, result.value());
    }

    /**
     * Test data class
     */
    private record TestObject(String name, int value) {}
}