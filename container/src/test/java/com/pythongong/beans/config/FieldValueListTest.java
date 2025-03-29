
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Test class for verifying the functionality of field value list operations.
 * This test suite ensures that field values are properly handled and processed
 * within the container context.
 *
 * @author Cheng Gong
 * @since 2025-03-18
 */
public class FieldValueListTest {

    private FieldValueList fieldValueList;

    @BeforeEach
    void setUp() {
        fieldValueList = new FieldValueList();
    }

    @Test
    void testAddAndGetFieldValue() {
        FieldValue fieldValue = new FieldValue("name", "value");
        fieldValueList.add(fieldValue);

        assertEquals(fieldValue, fieldValueList.getFieldValue("name"));
    }

    @Test
    void testGetNonExistentFieldValue() {
        assertNull(fieldValueList.getFieldValue("nonexistent"));
    }

    @Test
    void testIteratorInReverseOrder() {
        FieldValue first = new FieldValue("first", "1");
        FieldValue second = new FieldValue("second", "2");
        FieldValue third = new FieldValue("third", "3");

        fieldValueList.add(first);
        fieldValueList.add(second);
        fieldValueList.add(third);

        Iterator<FieldValue> iterator = fieldValueList.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(third, iterator.next());
        assertEquals(second, iterator.next());
        assertEquals(first, iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    void testIteratorThrowsNoSuchElementException() {
        Iterator<FieldValue> iterator = fieldValueList.iterator();
        assertFalse(iterator.hasNext());
        assertThrows(NoSuchElementException.class, iterator::next);
    }

    @Test
    void testSize() {
        assertEquals(0, fieldValueList.size());

        fieldValueList.add(new FieldValue("test", "value"));
        assertEquals(1, fieldValueList.size());

        fieldValueList.add(new FieldValue("test2", "value2"));
        assertEquals(2, fieldValueList.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(fieldValueList.isEmpty());

        fieldValueList.add(new FieldValue("test", "value"));
        assertFalse(fieldValueList.isEmpty());
    }

    @Test
    void testEqualsAndHashCode() {
        FieldValueList list1 = new FieldValueList();
        FieldValueList list2 = new FieldValueList();

        assertEquals(list1, list2);
        assertEquals(list1.hashCode(), list2.hashCode());

        FieldValue value = new FieldValue("test", "value");
        list1.add(value);
        list2.add(value);

        assertEquals(list1, list2);
        assertEquals(list1.hashCode(), list2.hashCode());
    }
}