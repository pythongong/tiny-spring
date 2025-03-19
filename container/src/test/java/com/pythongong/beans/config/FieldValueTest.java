/*
 * Copyright (c) 2024
 * [Your Name or Organization]
 *
 * Licensed under the MIT License (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://opensource.org/licenses/MIT
 */

package com.pythongong.beans.config;

import com.pythongong.exception.BeansException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for FieldValue.
 * Contains unit tests to verify the behavior of FieldValue class
 * which is responsible for holding field name-value pairs for bean properties.
 */
class FieldValueTest {

    /**
     * Tests that the constructor correctly creates a FieldValue instance
     * with valid name and value parameters.
     */
    @Test
    void constructorShouldCreateInstanceWithValidNameAndValue() {
        String name = "testProperty";
        Object value = "testValue";
        
        FieldValue fieldValue = new FieldValue(name, value);
        
        assertEquals(name, fieldValue.name());
        assertEquals(value, fieldValue.value());
    }

    /**
     * Tests that the constructor throws BeansException when
     * the name parameter is null.
     */
    @Test 
    void constructorShouldThrowExceptionWhenNameIsNull() {
        assertThrows(BeansException.class, () -> {
            new FieldValue(null, "testValue");
        });
    }

    /**
     * Tests that the constructor throws BeansException when
     * the name parameter is an empty string.
     */
    @Test
    void constructorShouldThrowExceptionWhenNameIsEmpty() {
        assertThrows(BeansException.class, () -> {
            new FieldValue("", "testValue");
        });
    }

    /**
     * Tests that the FieldValue correctly accepts a BeanReference
     * as its value parameter.
     */
    @Test
    void shouldAcceptBeanReferenceAsValue() {
        String name = "testProperty";
        BeanReference beanRef = new BeanReference("testBean");
        
        FieldValue fieldValue = new FieldValue(name, beanRef);
        
        assertEquals(name, fieldValue.name());
        assertEquals(beanRef, fieldValue.value());
    }
}