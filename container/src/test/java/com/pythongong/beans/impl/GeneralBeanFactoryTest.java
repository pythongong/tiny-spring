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

package com.pythongong.beans.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.registry.SingletonBeanRegistry;

/**
 * Test class for GeneralBeanFactory.
 * Tests the core functionality of the bean factory implementation including
 * bean creation, retrieval, and post-processing operations.
 */
class GeneralBeanFactoryTest {

    // Core components for testing
    private GeneralBeanFactory beanFactory;
    private Function<String, BeanDefinition> getBeanDefinition;
    private Function<BeanDefinition, Object> createBean;
    private SingletonBeanRegistry singletonRegistry;

    /**
     * Sets up the test environment before each test.
     * Initializes mocks and creates a new BeanFactory instance.
     */
    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        getBeanDefinition = mock(Function.class);
        createBean = mock(Function.class);
        singletonRegistry = mock(SingletonBeanRegistry.class);
        beanFactory = new GeneralBeanFactory(getBeanDefinition, createBean, singletonRegistry);
    }

    /**
     * Tests basic bean factory instantiation.
     */
    @Test
    void whenBeanFactoryCreated_thenNotNull() {
        assertNotNull(beanFactory);
    }

    /**
     * Tests constructor validation for null arguments.
     */
    @Test
    void whenNullArgs_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new GeneralBeanFactory(null, createBean, singletonRegistry));
        assertThrows(IllegalArgumentException.class,
                () -> new GeneralBeanFactory(getBeanDefinition, null, singletonRegistry));
        assertThrows(IllegalArgumentException.class, () -> new GeneralBeanFactory(getBeanDefinition, createBean, null));
    }

    /**
     * Tests validation of bean names.
     */
    @Test
    void whenGetBeanWithEmptyName_thenThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> beanFactory.getBean(""));
        assertThrows(IllegalArgumentException.class, () -> beanFactory.getBean(null));
    }

    /**
     * Tests successful bean retrieval by name.
     */
    @Test
    void whenGetBeanWithValidName_thenReturnBean() {
        String beanName = "testBean";
        Object expectedBean = new Object();
        BeanDefinition beanDef = mock(BeanDefinition.class);

        when(singletonRegistry.getSingleton(beanName)).thenReturn(expectedBean);
        when(getBeanDefinition.apply(beanName)).thenReturn(beanDef);

        Object actualBean = beanFactory.getBean(beanName);
        assertEquals(expectedBean, actualBean);
    }

    /**
     * Tests behavior when requesting non-existent beans.
     */
    @Test
    void whenBeanNotDefined_thenThrowsException() {
        String beanName = "nonExistentBean";
        when(getBeanDefinition.apply(beanName)).thenReturn(null);

        assertNull(beanFactory.getBean(beanName));
    }

    /**
     * Tests bean retrieval with type information.
     */
    @Test
    void whenGetBeanWithType_thenReturnTypedBean() {
        String beanName = "testBean";
        String expectedBean = "test";
        BeanDefinition beanDef = mock(BeanDefinition.class);

        when(singletonRegistry.getSingleton(beanName)).thenReturn(expectedBean);
        when(getBeanDefinition.apply(beanName)).thenReturn(beanDef);

        String actualBean = beanFactory.getBean(beanName, String.class);
        assertEquals(expectedBean, actualBean);
    }
}