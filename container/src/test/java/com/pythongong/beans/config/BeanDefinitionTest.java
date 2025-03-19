/*
 * MIT License
 *
 * Copyright (c) 2023 [Your Name or Organization]
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

package com.pythongong.beans.config;

import com.pythongong.enums.ScopeEnum;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for BeanDefinition
 * Verifies the correct creation and behavior of BeanDefinition objects
 * with various configurations.
 */
class BeanDefinitionTest {

    /**
     * Tests creation of a BeanDefinition with default values.
     * Verifies that unspecified properties are set to their expected defaults.
     */
    @Test
    void shouldCreateBeanDefinitionWithDefaultValues() {
        BeanDefinition beanDefinition = BeanDefinition.builder()
                .beanName("testBean")
                .beanClass(String.class)
                .build();

        assertNotNull(beanDefinition);
        assertEquals("testBean", beanDefinition.beanName());
        assertEquals(String.class, beanDefinition.beanClass());
        assertNotNull(beanDefinition.fieldValueList());
        assertTrue(beanDefinition.fieldValueList().isEmpty());
        assertEquals(ScopeEnum.SINGLETON, beanDefinition.scope());
        assertNull(beanDefinition.initMethod());
        assertNull(beanDefinition.destroyMethod());
        assertNull(beanDefinition.constructor());
    }

    /**
     * Tests creation of a BeanDefinition with custom values.
     * Verifies that explicitly set properties are properly maintained.
     */
    @Test 
    void shouldCreateBeanDefinitionWithCustomValues() {
        FieldValueList fieldValues = new FieldValueList();
        BeanDefinition beanDefinition = BeanDefinition.builder()
                .beanName("customBean")
                .beanClass(Integer.class)
                .fieldValueList(fieldValues)
                .scope(ScopeEnum.PROTOTYPE)
                .build();

        assertNotNull(beanDefinition);
        assertEquals("customBean", beanDefinition.beanName());
        assertEquals(Integer.class, beanDefinition.beanClass());
        assertSame(fieldValues, beanDefinition.fieldValueList());
        assertEquals(ScopeEnum.PROTOTYPE, beanDefinition.scope());
    }

    /**
     * Tests that fieldValueList is never null, even when explicitly set to null.
     * Verifies the null-safety behavior of the builder.
     */
    @Test
    void shouldNeverHaveNullFieldValueList() {
        BeanDefinition beanDefinition = BeanDefinition.builder()
                .beanName("testBean")
                .beanClass(String.class)
                .fieldValueList(null)
                .build();

        assertNotNull(beanDefinition.fieldValueList());
        assertTrue(beanDefinition.fieldValueList().isEmpty());
    }
}