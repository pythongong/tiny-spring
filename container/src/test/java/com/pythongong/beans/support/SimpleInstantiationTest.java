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

import java.lang.reflect.Constructor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pythongong.exception.BeansException;
import com.pythongong.test.utils.TestBean;

/**
 * Unit tests for {@link SimpleInstantiation}.
 * Tests the bean instantiation functionality using constructors.
 *
 * @author Cheng Gong
 */
class SimpleInstantiationTest {

    /**
     * The instantiation strategy being tested
     */
    private SimpleInstantiation instantiation;

    @BeforeEach
    void setUp() {
        instantiation = new SimpleInstantiation();
    }

    /**
     * Tests instantiation with default constructor
     */
    @Test
    @DisplayName("Should create instance using default constructor")
    void shouldCreateInstanceUsingDefaultConstructor() {
        // When
        Object result = instantiation.instance(TestBean.class, null, null);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof TestBean);
    }

    /**
     * Tests instantiation with parameterized constructor
     */
    @Test
    @DisplayName("Should create instance using parameterized constructor")
    void shouldCreateInstanceUsingParameterizedConstructor() throws Exception {
        // Given
        Constructor<TestBeanWithParam> constructor = TestBeanWithParam.class.getConstructor(String.class);
        Object[] args = new Object[]{"test"};

        // When
        Object result = instantiation.instance(TestBeanWithParam.class, constructor, args);

        // Then
        assertNotNull(result);
        assertTrue(result instanceof TestBeanWithParam);
        assertEquals("test", ((TestBeanWithParam) result).getName());
    }

    /**
     * Tests validation of null class parameter
     */
    @Test
    @DisplayName("Should throw exception when class is null")
    void shouldThrowExceptionWhenClassIsNull() {
        assertThrows(BeansException.class, 
            () -> instantiation.instance(null, null, null),
            "Should throw IllegalArgumentException for null class"
        );
    }

    /**
     * Tests handling of instantiation errors
     */
    @Test
    @DisplayName("Should throw exception when instantiation fails")
    void shouldThrowExceptionWhenInstantiationFails() {
        assertThrows(BeansException.class, 
            () -> instantiation.instance(TestBeanWithPrivateConstructor.class, null, null),
            "Should throw BeansException when instantiation fails"
        );
    }

    /**
     * Tests handling of constructor parameter mismatch
     */
    @Test
    @DisplayName("Should throw exception when constructor parameters don't match")
    void shouldThrowExceptionWhenConstructorParametersDontMatch() throws Exception {
        // Given
        Constructor<TestBeanWithParam> constructor = TestBeanWithParam.class.getConstructor(String.class);
        Object[] args = new Object[]{1}; // Wrong parameter type

        // When/Then
        assertThrows(BeansException.class, 
            () -> instantiation.instance(TestBeanWithParam.class, constructor, args),
            "Should throw BeansException when constructor parameters don't match"
        );
    }

    
}



class TestBeanWithParam {
    private final String name;
    
    public TestBeanWithParam(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class TestBeanWithPrivateConstructor {
    private TestBeanWithPrivateConstructor() {}
}
