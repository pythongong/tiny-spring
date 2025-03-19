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
package com.pythongong.test.utils;

import java.util.Properties;

/**
 * Utility class for common test operations.
 *
 * @author Cheng Gong
 */
public class TestUtils {
    
    public static Properties createTestProperties() {
        Properties properties = new Properties();
        properties.setProperty("test.string.value", "testValue");
        properties.setProperty("test.integer.value", "42");
        properties.setProperty("test.boolean.value", "true");
        return properties;
    }
    
    public static class TestBean {
        private String name;
        private int value;

        public TestBean() {
        }

        public TestBean(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }
}