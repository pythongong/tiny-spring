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
import org.junit.jupiter.api.BeforeEach;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for the FieldValueList class which manages a collection
 * of FieldValue objects for bean property configuration.
 *
 * @author Cheng Gong
 */
@DisplayName("FieldValueList Tests")
class FieldValueListTest {

    private FieldValueList fieldValueList;

    @BeforeEach
    void setUp() {
        fieldValueList = new FieldValueList();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create empty list")
        void shouldCreateEmptyList() {
            assertAll(
                () -> assertTrue(fieldValueList.isEmpty(), "New list should be empty"),
                () -> assertEquals(0, fieldValueList.size(), "New list should have size 0")
            );
        }
    }

    @Nested
    @DisplayName("Add Operation Tests")
    class AddOperationTests {

        @Test
        @DisplayName("Should add field value successfully")
        void shouldAddFieldValueSuccessfully() {
            // Arrange
            FieldValue fieldValue = new FieldValue("name", "value");

            // Act
            fieldValueList.add(fieldValue);

            // Assert
            assertAll(
                () -> assertFalse(fieldValueList.isEmpty(), "List should not be empty after adding"),
                () -> assertEquals(1, fieldValueList.size(), "List should have size 1"),
                () -> assertEquals(fieldValue, fieldValueList.getfieldValue("name"),
                    "Should retrieve added field value")
            );
        }

        @Test
        @DisplayName("Should allow multiple field values with different names")
        void shouldAllowMultipleFieldValues() {
            // Arrange
            FieldValue value1 = new FieldValue("name1", "value1");
            FieldValue value2 = new FieldValue("name2", "value2");

            // Act
            fieldValueList.add(value1);
            fieldValueList.add(value2);

            // Assert
            assertAll(
                () -> assertEquals(2, fieldValueList.size(), "List should have size 2"),
                () -> assertEquals(value1, fieldValueList.getfieldValue("name1"),
                    "Should retrieve first field value"),
                () -> assertEquals(value2, fieldValueList.getfieldValue("name2"),
                    "Should retrieve second field value")
            );
        }

        @Test
        @DisplayName("Should allow null field values")
        void shouldAllowNullFieldValues() {
            // Arrange & Act
            fieldValueList.add(null);

            // Assert
            assertEquals(1, fieldValueList.size(), "Should increase size even with null value");
        }
    }

    @Nested
    @DisplayName("Get Operation Tests")
    class GetOperationTests {

        @Test
        @DisplayName("Should return null for non-existent property")
        void shouldReturnNullForNonExistentProperty() {
            assertNull(fieldValueList.getfieldValue("nonexistent"),
                "Should return null for non-existent property");
        }

        @Test
        @DisplayName("Should return correct field value for existing property")
        void shouldReturnCorrectFieldValue() {
            // Arrange
            FieldValue expected = new FieldValue("test", "value");
            fieldValueList.add(expected);

            // Act
            FieldValue actual = fieldValueList.getfieldValue("test");

            // Assert
            assertEquals(expected, actual, "Should return correct field value");
        }

        @Test
        @DisplayName("Should handle case-sensitive property names")
        void shouldHandleCaseSensitivePropertyNames() {
            // Arrange
            FieldValue value = new FieldValue("Test", "value");
            fieldValueList.add(value);

            // Assert
            assertAll(
                () -> assertNotNull(fieldValueList.getfieldValue("Test"),
                    "Should find exact case match"),
                () -> assertNull(fieldValueList.getfieldValue("test"),
                    "Should not find different case")
            );
        }
    }

    @Nested
    @DisplayName("Iterator Tests")
    class IteratorTests {

        @Test
        @DisplayName("Should iterate in reverse order")
        void shouldIterateInReverseOrder() {
            // Arrange
            List<FieldValue> expected = new ArrayList<>();
            expected.add(new FieldValue("name1", "value1"));
            expected.add(new FieldValue("name2", "value2"));
            expected.add(new FieldValue("name3", "value3"));

            expected.forEach(fieldValueList::add);

            // Act & Assert
            Iterator<FieldValue> iterator = fieldValueList.iterator();
            for (int i = expected.size() - 1; i >= 0; i--) {
                assertTrue(iterator.hasNext(), "Iterator should have next element");
                assertEquals(expected.get(i), iterator.next(),
                    "Should return elements in reverse order");
            }
            assertFalse(iterator.hasNext(), "Iterator should be exhausted");
        }

        @Test
        @DisplayName("Should throw NoSuchElementException when exhausted")
        void shouldThrowNoSuchElementException() {
            // Arrange
            Iterator<FieldValue> iterator = fieldValueList.iterator();

            // Assert
            assertThrows(NoSuchElementException.class, iterator::next,
                "Should throw NoSuchElementException when no more elements");
        }

        @Test
        @DisplayName("Should handle empty list iteration")
        void shouldHandleEmptyListIteration() {
            // Act
            Iterator<FieldValue> iterator = fieldValueList.iterator();

            // Assert
            assertFalse(iterator.hasNext(), "Empty list iterator should have no next element");
        }
    }

    @Nested
    @DisplayName("Utility Method Tests")
    class UtilityMethodTests {

        @Test
        @DisplayName("Should report correct size")
        void shouldReportCorrectSize() {
            // Arrange & Act
            fieldValueList.add(new FieldValue("name1", "value1"));
            fieldValueList.add(new FieldValue("name2", "value2"));

            // Assert
            assertEquals(2, fieldValueList.size(), "Should report correct size");
        }

        @Test
        @DisplayName("Should report empty status correctly")
        void shouldReportEmptyStatusCorrectly() {
            // Assert initial state
            assertTrue(fieldValueList.isEmpty(), "New list should be empty");

            // Act
            fieldValueList.add(new FieldValue("name", "value"));

            // Assert after adding
            assertFalse(fieldValueList.isEmpty(), "List should not be empty after adding");
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
            // Arrange
            FieldValueList list1 = new FieldValueList();
            FieldValueList list2 = new FieldValueList();
            FieldValueList list3 = new FieldValueList();

            FieldValue value = new FieldValue("name", "value");
            list1.add(value);
            list2.add(value);
            list3.add(new FieldValue("different", "value"));

            // Assert
            assertAll(
                () -> assertEquals(list1, list1, "Same object should be equal"),
                () -> assertEquals(list1, list2, "Lists with same values should be equal"),
                () -> assertNotEquals(list1, list3, "Lists with different values should not be equal"),
                () -> assertNotEquals(list1, null, "List should not equal null"),
                () -> assertNotEquals(list1, new Object(), "List should not equal different type")
            );
        }

        @Test
        @DisplayName("Should implement hashCode consistently")
        void shouldImplementHashCodeConsistently() {
            // Arrange
            FieldValueList list1 = new FieldValueList();
            FieldValueList list2 = new FieldValueList();
            FieldValue value = new FieldValue("name", "value");
            list1.add(value);
            list2.add(value);

            // Assert
            assertAll(
                () -> assertEquals(list1.hashCode(), list2.hashCode(),
                    "Hash codes should be equal for equal lists"),
                () -> assertEquals(list1.hashCode(), list1.hashCode(),
                    "Hash code should be consistent")
            );
        }
    }
}