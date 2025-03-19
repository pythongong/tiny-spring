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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import com.pythongong.exception.BeansException;

/**
 * Tests for the FieldValue record class which represents a property
 * value pair used in bean property configuration.
 *
 * @author Cheng Gong
 */
@DisplayName("FieldValue Tests")
class FieldValueTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create field value with valid parameters")
        void shouldCreateFieldValueWithValidParameters() {
            // Act
            FieldValue fieldValue = new FieldValue("name", "testValue");

            // Assert
            assertAll(
                () -> assertEquals("name", fieldValue.name(), "Field name should match"),
                () -> assertEquals("testValue", fieldValue.value(), "Field value should match")
            );
        }

        @Test
        @DisplayName("Should accept null value")
        void shouldAcceptNullValue() {
            // Act
            FieldValue fieldValue = new FieldValue("name", null);

            // Assert
            assertAll(
                () -> assertEquals("name", fieldValue.name(), "Field name should match"),
                () -> assertNull(fieldValue.value(), "Field value should be null")
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "\t", "\n"})
        @DisplayName("Should throw exception for invalid name")
        void shouldThrowExceptionForInvalidName(String invalidName) {
            // Assert
            assertThrows(BeansException.class,
                () -> new FieldValue(invalidName, "value"),
                "Should throw BeansException for invalid name");
        }
    }

    @Nested
    @DisplayName("Value Type Tests")
    class ValueTypeTests {

        @Test
        @DisplayName("Should support string value")
        void shouldSupportStringValue() {
            // Act
            FieldValue fieldValue = new FieldValue("name", "testValue");

            // Assert
            assertAll(
                () -> assertEquals("name", fieldValue.name()),
                () -> assertEquals("testValue", fieldValue.value()),
                () -> assertTrue(fieldValue.value() instanceof String)
            );
        }

        @Test
        @DisplayName("Should support integer value")
        void shouldSupportIntegerValue() {
            // Act
            FieldValue fieldValue = new FieldValue("age", 25);

            // Assert
            assertAll(
                () -> assertEquals("age", fieldValue.name()),
                () -> assertEquals(25, fieldValue.value()),
                () -> assertTrue(fieldValue.value() instanceof Integer)
            );
        }

        @Test
        @DisplayName("Should support bean reference value")
        void shouldSupportBeanReferenceValue() {
            // Arrange
            BeanReference reference = new BeanReference("referencedBean");
            
            // Act
            FieldValue fieldValue = new FieldValue("reference", reference);

            // Assert
            assertAll(
                () -> assertEquals("reference", fieldValue.name()),
                () -> assertEquals(reference, fieldValue.value()),
                () -> assertTrue(fieldValue.value() instanceof BeanReference)
            );
        }
    }

    @Nested
    @DisplayName("Record Implementation Tests")
    class RecordImplementationTests {

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
            // Arrange
            FieldValue value1 = new FieldValue("name", "testValue");
            FieldValue value2 = new FieldValue("name", "testValue");
            FieldValue value3 = new FieldValue("name", "differentValue");
            FieldValue value4 = new FieldValue("differentName", "testValue");

            // Assert
            assertAll(
                () -> assertEquals(value1, value1, "Same object should be equal"),
                () -> assertEquals(value1, value2, "Objects with same values should be equal"),
                () -> assertNotEquals(value1, value3, "Objects with different values should not be equal"),
                () -> assertNotEquals(value1, value4, "Objects with different names should not be equal"),
                () -> assertNotEquals(value1, null, "Object should not equal null"),
                () -> assertNotEquals(value1, new Object(), "Object should not equal different type")
            );
        }

        @Test
        @DisplayName("Should implement hashCode consistently")
        void shouldImplementHashCodeConsistently() {
            // Arrange
            FieldValue value1 = new FieldValue("name", "testValue");
            FieldValue value2 = new FieldValue("name", "testValue");

            // Assert
            assertAll(
                () -> assertEquals(value1.hashCode(), value2.hashCode(), 
                    "Hash codes should be equal for equal objects"),
                () -> assertEquals(value1.hashCode(), value1.hashCode(), 
                    "Hash code should be consistent")
            );
        }

        @Test
        @DisplayName("Should implement toString meaningfully")
        void shouldImplementToStringMeaningfully() {
            // Arrange
            FieldValue fieldValue = new FieldValue("name", "testValue");

            // Act
            String toString = fieldValue.toString();

            // Assert
            assertAll(
                () -> assertTrue(toString.contains("name"), 
                    "toString should contain field name"),
                () -> assertTrue(toString.contains("testValue"), 
                    "toString should contain field value")
            );
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle special characters in name")
        void shouldHandleSpecialCharactersInName() {
            // Arrange & Act
            FieldValue fieldValue = new FieldValue("special@#$%", "value");

            // Assert
            assertEquals("special@#$%", fieldValue.name(), 
                "Should preserve special characters in name");
        }

        @Test
        @DisplayName("Should handle very long name")
        void shouldHandleVeryLongName() {
            // Arrange
            String longName = "a".repeat(1000);

            // Act
            FieldValue fieldValue = new FieldValue(longName, "value");

            // Assert
            assertEquals(longName, fieldValue.name(), 
                "Should handle long names correctly");
        }
    }
}