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
package com.pythongong.beans.support;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.config.FieldValueList;
import com.pythongong.context.event.ApplicationEventMulticaster;
import com.pythongong.context.event.GeneralApplicationEventMulticaster;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.exception.BeansException;
import com.pythongong.exception.NoSuchBeanException;
import com.pythongong.util.ClassUtils;

/**
 * Unit tests for {@link DefaultConfigurableListableBeanFactory}.
 * Tests the core functionality of the bean factory implementation including
 * bean registration, retrieval, and lifecycle management.
 *
 * @author Cheng Gong
 */
@ExtendWith(MockitoExtension.class)
class DefaultConfigurableListableBeanFactoryTest {

    /**
     * The bean factory instance being tested
     */
    private DefaultConfigurableListableBeanFactory beanFactory;

    /**
     * Mock bean post processor for testing
     */
    @Mock
    private BeanPostProcessor mockBeanPostProcessor;

    /**
     * Sets up the test environment before each test
     */
    @BeforeEach
    void setUp() {
        beanFactory = new DefaultConfigurableListableBeanFactory();
    }

    /**
     * Tests the registration and retrieval of a bean definition
     */
    @Test
    @DisplayName("Should register and retrieve bean definition successfully")
    void shouldRegisterAndRetrieveBeanDefinition() {
        // Given
        BeanDefinition beanDefinition = BeanDefinition.builder()
            .beanName("testBean")
            .beanClass(TestBean.class)
            .scope(ScopeEnum.SINGLETON)
            .fieldValueList(new FieldValueList())
            .build();

        // When
        beanFactory.registerBeanDefinition(beanDefinition);

        // Then
        BeanDefinition retrievedDefinition = beanFactory.getBeanDefinition(beanDefinition.beanName());
        assertNotNull(retrievedDefinition, "Retrieved bean definition should not be null");
        assertEquals(beanDefinition, retrievedDefinition, "Retrieved bean definition should match the registered one");
    }

    /**
     * Tests the registration validation for null bean definition
     */
    @Test
    @DisplayName("Should throw exception when registering null bean definition")
    void shouldThrowExceptionWhenRegisteringNullBeanDefinition() {
        assertThrows(BeansException.class, 
            () -> beanFactory.registerBeanDefinition(null),
            "Should throw BeansException for null bean definition"
        );
    }

    /**
     * Tests the registration validation for bean definition with empty name
     */
    @Test
    @DisplayName("Should throw exception when registering bean definition with empty name")
    void shouldThrowExceptionWhenRegisteringBeanDefinitionWithEmptyName() {
        // Given
        BeanDefinition beanDefinition = BeanDefinition.builder()
            .beanName("")
            .beanClass(TestBean.class)
            .build();

        // When/Then
        assertThrows(BeansException.class, 
            () -> beanFactory.registerBeanDefinition(beanDefinition),
            "Should throw BeansException for empty bean name"
        );
    }

    

    /**
     * Tests the initialization of the application event multicaster
     */
    @Test
    @DisplayName("Should initialize and register ApplicationEventMulticaster")
    void shouldInitializeApplicationEventMulticaster() {
        // When
        ApplicationEventMulticaster multicaster = beanFactory.initApplicationEventMulticaster();

        // Then
        assertNotNull(multicaster, "ApplicationEventMulticaster should not be null");
        assertTrue(multicaster instanceof GeneralApplicationEventMulticaster, 
            "Multicaster should be instance of GeneralApplicationEventMulticaster");
        
        Object registeredMulticaster = beanFactory.getSingleton(
            ClassUtils.APPLICATION_EVENT_MULTICASTER_BEAN_NAME);
        assertNotNull(registeredMulticaster, "Registered multicaster should not be null");
        assertEquals(multicaster, registeredMulticaster, 
            "Registered multicaster should match the initialized one");
    }

    /**
     * Tests adding and applying a bean post processor
     */
    @Test
    @DisplayName("Should add and apply bean post processor")
    void shouldAddAndApplyBeanPostProcessor() {
        // Given
        String beanName = "testBean";
        Object bean = new Object();
        
        when(mockBeanPostProcessor.postProcessBeforeInitialization(any(), anyString()))
            .thenReturn(bean);
        when(mockBeanPostProcessor.postProcessAfterInitialization(any(), anyString()))
            .thenReturn(bean);

        // When
        beanFactory.addBeanPostProcessor(mockBeanPostProcessor);
        Object resultBefore = beanFactory.applyBeanPostProcessorsBeforeInitialization(bean, beanName);
        Object resultAfter = beanFactory.applyBeanPostProcessorsAfterInitialization(bean, beanName);

        // Then
        assertNotNull(resultBefore, "Result of before initialization should not be null");
        assertNotNull(resultAfter, "Result of after initialization should not be null");
        verify(mockBeanPostProcessor).postProcessBeforeInitialization(bean, beanName);
        verify(mockBeanPostProcessor).postProcessAfterInitialization(bean, beanName);
    }

    /**
     * Tests getting beans of a specific type when no matching beans exist
     */
    @Test
    @DisplayName("Should throw NoSuchBeanException when no beans of type exist")
    void shouldThrowExceptionWhenNoBeansOfTypeExist() {
        // When/Then
        NoSuchBeanException exception = assertThrows(NoSuchBeanException.class, 
            () -> beanFactory.getBeansOfType(String.class),
            "Should throw NoSuchBeanException when no beans of type exist"
        );
        
        assertNotNull(exception.getMessage(), "Exception message should not be null");
    }

    /**
     * Tests the pre-instantiation of singletons
     */
    @Test
    @DisplayName("Should pre-instantiate singletons")
    void shouldPreInstantiateSingletons() {
        // Given
        BeanDefinition beanDefinition = BeanDefinition.builder()
            .beanName("testBean")
            .beanClass(TestBean.class)
            .scope(ScopeEnum.SINGLETON)
            .build();

        beanFactory.registerBeanDefinition(beanDefinition);

        // When
        beanFactory.preInstantiateSingletons();

        // Then
        assertNotNull(beanFactory.getSingleton("testBean"), 
            "Singleton bean should be instantiated");
    }

    /**
     * Tests the behavior of processing null bean in post processors
     */
    @Test
    @DisplayName("Should handle null result from post processor")
    void shouldHandleNullResultFromPostProcessor() {
        // Given
        String beanName = "testBean";
        Object bean = new Object();
        
        when(mockBeanPostProcessor.postProcessBeforeInitialization(any(), anyString()))
            .thenReturn(null);

        // When
        beanFactory.addBeanPostProcessor(mockBeanPostProcessor);
        Object result = beanFactory.applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // Then
        assertEquals(bean, result, "Should return original bean when processor returns null");
        verify(mockBeanPostProcessor).postProcessBeforeInitialization(bean, beanName);
    }

    
    
}

/**
* Test support class
*/
class TestBean {}