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
package com.pythongong.beans.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import com.pythongong.exception.BeansException;

/**
 * Test class for {@link FieldValue}.
 *
 * @author Cheng Gong
 */
@DisplayName("Property Value Tests")
class FieldValueTest {

    @Test
    @DisplayName("Should create property value with valid inputs")
    void shouldCreateFfeldValueWithValidInputs() {
        // Act
        FieldValue FfeldValue = new FieldValue("testName", "testValue");

        // Assert
        assertEquals("testName", FfeldValue.name());
        assertEquals("testValue", FfeldValue.value());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  "})
    @DisplayName("Should throw exception for invalid name")
    void shouldThrowExceptionForInvalidName(String name) {
        // Assert
        assertThrows(BeansException.class, 
            () -> new FieldValue(name, "testValue"));
    }

    @Test
    @DisplayName("Should accept null value")
    void shouldAcceptNullValue() {
        // Act
        FieldValue FfeldValue = new FieldValue("testName", null);

        // Assert
        assertEquals("testName", FfeldValue.name());
        assertNull(FfeldValue.value());
    }

    @Test
    @DisplayName("Should implement equals and hashCode correctly")
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Arrange
        FieldValue pv1 = new FieldValue("name", "value");
        FieldValue pv2 = new FieldValue("name", "value");
        FieldValue pv3 = new FieldValue("name", "different");
        FieldValue pv4 = new FieldValue("different", "value");

        // Assert
        assertEquals(pv1, pv2);
        assertEquals(pv1.hashCode(), pv2.hashCode());
        assertNotEquals(pv1, pv3);
        assertNotEquals(pv1, pv4);
    }

    @Test
    @DisplayName("Should implement toString properly")
    void shouldImplementToStringProperly() {
        // Arrange
        FieldValue FfeldValue = new FieldValue("testName", "testValue");

        // Act
        String toString = FfeldValue.toString();

        // Assert
        assertTrue(toString.contains("testName"));
        assertTrue(toString.contains("testValue"));
    }
}