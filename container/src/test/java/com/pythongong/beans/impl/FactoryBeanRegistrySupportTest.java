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
package com.pythongong.beans.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.beans.config.FactoryBean;
import com.pythongong.exception.BeansException;
import com.pythongong.test.ioc.normal.TestBean;

/**
 * Unit tests for {@link FactoryBeanRegistrySupport}.
 * Tests the factory bean caching and object creation functionality.
 *
 * @author Cheng Gong
 */
@ExtendWith(MockitoExtension.class)
class FactoryBeanRegistrySupportTest {

    /**
     * The registry support being tested
     */
    private FactoryBeanRegistrySupport registrySupport;

    @Mock
    private FactoryBean<TestBean> mockFactoryBean;

    @BeforeEach
    void setUp() {
        registrySupport = new FactoryBeanRegistrySupport();
    }

    /**
     * Tests retrieval of non-existent cached object
     */
    @Test
    @DisplayName("Should return null for non-existent cached object")
    void shouldReturnNullForNonExistentCachedObject() {
        // When
        Object result = registrySupport.getCachedObjectForFactoryBean("nonExistentBean");

        // Then
        assertNull(result);
    }

    /**
     * Tests validation of empty bean name
     */
    @Test
    @DisplayName("Should throw exception for empty bean name")
    void shouldThrowExceptionForEmptyBeanName() {
        assertThrows(IllegalArgumentException.class,
                () -> registrySupport.getCachedObjectForFactoryBean(""),
                "Should throw IllegalArgumentException for empty bean name");
    }

    /**
     * Tests object creation for singleton-scoped factory bean
     */
    @Test
    @DisplayName("Should create and cache object for singleton factory bean")
    void shouldCreateAndCacheObjectForSingletonFactoryBean() throws Exception {
        // Given
        String beanName = "testBean";
        TestBean expectedObject = new TestBean();

        when(mockFactoryBean.getObject()).thenReturn(expectedObject);
        when(mockFactoryBean.isSingleton()).thenReturn(true);

        // When
        TestBean result1 = (TestBean) registrySupport.getObjectFromFactoryBean(mockFactoryBean, beanName);
        TestBean result2 = (TestBean) registrySupport.getObjectFromFactoryBean(mockFactoryBean, beanName);

        // Then
        assertNotNull(result1);
        assertEquals(result1, result2);
        assertEquals(expectedObject, result1);
    }

    /**
     * Tests object creation for prototype-scoped factory bean
     */
    @Test
    @DisplayName("Should create new object for prototype factory bean")
    void shouldCreateNewObjectForPrototypeFactoryBean() throws Exception {
        // Given
        String beanName = "testBean";

        FactoryBean<TestBean> factoryBean = new FactoryBean<TestBean>() {

            @Override
            public TestBean getObject() throws Exception {
                return new TestBean();
            }

            @Override
            public boolean isSingleton() {
                return false;
            }

        };

        // When
        TestBean result1 = (TestBean) registrySupport.getObjectFromFactoryBean(factoryBean, beanName);
        TestBean result2 = (TestBean) registrySupport.getObjectFromFactoryBean(factoryBean, beanName);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertTrue(result1 != result2);
    }

    /**
     * Tests handling of null object from factory bean
     */
    @Test
    @DisplayName("Should handle null object from factory bean")
    void shouldHandleNullObjectFromFactoryBean() throws Exception {
        // Given
        String beanName = "testBean";

        when(mockFactoryBean.getObject()).thenReturn(null);
        when(mockFactoryBean.isSingleton()).thenReturn(true);

        assertThrows(BeansException.class,
                () -> registrySupport.getObjectFromFactoryBean(mockFactoryBean, beanName),
                "Should throw IllegalArgumentException for null factory bean");
    }

    /**
     * Tests handling of factory bean exception
     */
    @Test
    @DisplayName("Should handle factory bean exception")
    void shouldHandleFactoryBeanException() throws Exception {
        // Given
        when(mockFactoryBean.getObject()).thenThrow(new Exception("Factory error"));

        // When/Then
        assertThrows(BeansException.class,
                () -> registrySupport.getObjectFromFactoryBean(mockFactoryBean,
                        "testBean"),
                "Should throw IllegalArgumentException when factory bean throws exception");
    }

    /**
     * Tests validation of null parameters
     * 
     * @throws Exception
     */
    @Test
    @DisplayName("Should validate null parameters")
    void shouldValidateNullParameters() throws Exception {
        assertThrows(IllegalArgumentException.class,
                () -> registrySupport.getObjectFromFactoryBean(null, "testBean"),
                "Should throw IllegalArgumentException for null factory bean");
        when(mockFactoryBean.isSingleton()).thenReturn(true);
        assertThrows(IllegalArgumentException.class,
                () -> registrySupport.getObjectFromFactoryBean(mockFactoryBean, null),
                "Should throw IllegalArgumentException for null bean definition");
    }

}