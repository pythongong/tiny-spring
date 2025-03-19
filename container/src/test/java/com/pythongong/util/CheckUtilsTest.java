/*
 * MIT License
 *
 * Copyright (c) 2024 [Your Name or Organization]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 */

package com.pythongong.util;

import com.pythongong.exception.BeansException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for CheckUtils utility methods.
 * Contains unit tests for null checking, empty array validation,
 * and string validation methods.
 */
class CheckUtilsTest {

    /**
     * Tests that nullArgs throws BeansException when given null input
     */
    @Test
    void nullArgs_WithNullObject_ThrowsBeansException() {
        String errorMsg = "Object should not be null";
        assertThrows(BeansException.class, () -> CheckUtils.nullArgs(null, errorMsg));
    }

    /**
     * Tests that nullArgs accepts non-null input without throwing
     */
    @Test 
    void nullArgs_WithNonNullObject_DoesNotThrow() {
        assertDoesNotThrow(() -> CheckUtils.nullArgs("test", "any message"));
    }

    /**
     * Tests that emptyArray throws BeansException when given null array
     */
    @Test
    void emptyArray_WithNullArray_ThrowsBeansException() {
        String errorMsg = "Array should not be null";
        assertThrows(BeansException.class, () -> CheckUtils.emptyArray(null, errorMsg));
    }

    /**
     * Tests that emptyArray throws BeansException when given empty array
     */
    @Test
    void emptyArray_WithEmptyArray_ThrowsBeansException() {
        String errorMsg = "Array should not be empty";
        Object[] emptyArray = new Object[0];
        assertThrows(BeansException.class, () -> CheckUtils.emptyArray(emptyArray, errorMsg));
    }

    /**
     * Tests that emptyArray accepts non-empty array without throwing
     */
    @Test
    void emptyArray_WithNonEmptyArray_DoesNotThrow() {
        Object[] nonEmptyArray = new Object[]{"test"};
        assertDoesNotThrow(() -> CheckUtils.emptyArray(nonEmptyArray, "any message"));
    }

    /**
     * Tests that emptyString throws BeansException when given null string
     */
    @Test
    void emptyString_WithNullString_ThrowsBeansException() {
        String errorMsg = "String should not be null";
        assertThrows(BeansException.class, () -> CheckUtils.emptyString(null, errorMsg));
    }

    /**
     * Tests that emptyString throws BeansException when given empty string
     */
    @Test
    void emptyString_WithEmptyString_ThrowsBeansException() {
        String errorMsg = "String should not be empty";
        assertThrows(BeansException.class, () -> CheckUtils.emptyString("", errorMsg));
    }

    /**
     * Tests that emptyString throws BeansException when given blank string
     */
    @Test
    void emptyString_WithBlankString_ThrowsBeansException() {
        String errorMsg = "String should not be blank";
        assertThrows(BeansException.class, () -> CheckUtils.emptyString("   ", errorMsg));
    }

    /**
     * Tests that emptyString accepts non-empty string without throwing
     */
    @Test
    void emptyString_WithNonEmptyString_DoesNotThrow() {
        assertDoesNotThrow(() -> CheckUtils.emptyString("test", "any message"));
    }
}