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

import static org.junit.jupiter.api.Assertions.*;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link ClassUtils}.
 *
 * @author Cheng Gong
 * @since 2025-03-20 11:25:29
 */
@DisplayName("ClassUtils Tests")
class ClassUtilsTest {

    @Test
    @DisplayName("Should get default ClassLoader")
    void shouldGetDefaultClassLoader() {
        ClassLoader loader = ClassUtils.getDefaultClassLoader();
        assertNotNull(loader);
        assertEquals(Thread.currentThread().getContextClassLoader(), loader);
    }

    @Test
    @DisplayName("Should find direct annotation")
    void shouldFindDirectAnnotation() {
        TestAnnotation annotation = ClassUtils.findAnnotation(
            DirectAnnotatedClass.class, 
            TestAnnotation.class
        );
        assertNotNull(annotation);
        assertEquals("direct", annotation.value());
    }

    @Test
    @DisplayName("Should find meta-annotation")
    void shouldFindMetaAnnotation() {
        TestAnnotation annotation = ClassUtils.findAnnotation(
            MetaAnnotatedClass.class, 
            TestAnnotation.class
        );
        assertNotNull(annotation);
        assertEquals("meta", annotation.value());
    }

    @Test
    @DisplayName("Should return null for non-existent annotation")
    void shouldReturnNullForNonExistentAnnotation() {
        TestAnnotation annotation = ClassUtils.findAnnotation(
            NonAnnotatedClass.class, 
            TestAnnotation.class
        );
        assertNull(annotation);
    }

    @Test
    @DisplayName("Should check empty array")
    void shouldCheckEmptyArray() {
        assertTrue(ClassUtils.isArrayEmpty(null));
        assertTrue(ClassUtils.isArrayEmpty(new Object[0]));
        assertFalse(ClassUtils.isArrayEmpty(new Object[] { "test" }));
    }

    @Test
    @DisplayName("Should check empty collection")
    void shouldCheckEmptyCollection() {
        assertTrue(ClassUtils.isCollectionEmpty(null));
        assertTrue(ClassUtils.isCollectionEmpty(new ArrayList<>()));
        
        List<String> nonEmptyList = new ArrayList<>();
        nonEmptyList.add("test");
        assertFalse(ClassUtils.isCollectionEmpty(nonEmptyList));
    }

    @Test
    @DisplayName("Should handle collection with null elements")
    void shouldHandleCollectionWithNullElements() {
        List<String> listWithNull = new ArrayList<>();
        listWithNull.add(null);
        assertFalse(ClassUtils.isCollectionEmpty(listWithNull));
    }

    @Test
    @DisplayName("Should handle array with null elements")
    void shouldHandleArrayWithNullElements() {
        Object[] arrayWithNull = new Object[] { null };
        assertFalse(ClassUtils.isArrayEmpty(arrayWithNull));
    }

    @Test
    @DisplayName("Should handle empty collection")
    void shouldHandleEmptyCollection() {
        assertTrue(ClassUtils.isCollectionEmpty(Collections.emptyList()));
    }



    // Test annotations and classes
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface TestAnnotation {
        String value();
    }

    @TestAnnotation(value = "meta")
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface MetaAnnotation {}

    @TestAnnotation(value = "direct")
    public static class DirectAnnotatedClass {}

    @MetaAnnotation
    public static class MetaAnnotatedClass {}

    public static class NonAnnotatedClass {}
}