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
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.pythongong.exception.BeansException;

/**
 * Unit tests for {@link SimpleInstantiation}.
 * Tests both successful instantiation cases and error scenarios.
 *
 * @author Cheng Gong
 */
class SimpleInstantiationTest {

    private SimpleInstantiation instantiationStrategy;

    @BeforeEach
    void setUp() {
        instantiationStrategy = new SimpleInstantiation();
    }

    @Nested
    @DisplayName("Default Constructor Tests")
    class DefaultConstructorTests {
        
        @Test
        @DisplayName("Should create instance using default constructor")
        void shouldCreateInstanceUsingDefaultConstructor() {
            // Act
            Object result = instantiationStrategy.instance(TestBean.class, null, null);
            
            // Assert
            assertNotNull(result);
            assertTrue(result instanceof TestBean);
        }

        @Test
        @DisplayName("Should throw BeansException when class is null")
        void shouldThrowExceptionWhenClassIsNull() {
            // Assert
            assertThrows(BeansException.class, 
                () -> instantiationStrategy.instance(null, null, null));
        }

        @Test
        @DisplayName("Should throw BeansException for class without default constructor")
        void shouldThrowExceptionForClassWithoutDefaultConstructor() {
            // Assert
            assertThrows(BeansException.class, 
                () -> instantiationStrategy.instance(NoDefaultConstructorBean.class, null, null));
        }
    }

    @Nested
    @DisplayName("Parameterized Constructor Tests")
    class ParameterizedConstructorTests {

        @Test
        @DisplayName("Should create instance using parameterized constructor")
        void shouldCreateInstanceUsingParameterizedConstructor() throws Exception {
            // Arrange
            Constructor<ParameterizedBean> constructor = ParameterizedBean.class.getConstructor(String.class, int.class);
            Object[] args = new Object[]{"test", 42};

            // Act
            Object result = instantiationStrategy.instance(ParameterizedBean.class, constructor, args);

            // Assert
            assertNotNull(result);
            assertTrue(result instanceof ParameterizedBean);
            ParameterizedBean bean = (ParameterizedBean) result;
            assertEquals("test", bean.getName());
            assertEquals(42, bean.getValue());
        }

        @Test
        @DisplayName("Should throw BeansException when constructor args don't match")
        void shouldThrowExceptionWhenConstructorArgsDoNotMatch() throws Exception {
            // Arrange
            Constructor<ParameterizedBean> constructor = ParameterizedBean.class.getConstructor(String.class, int.class);
            Object[] args = new Object[]{"test"}; // Missing one argument

            // Assert
            assertThrows(BeansException.class, 
                () -> instantiationStrategy.instance(ParameterizedBean.class, constructor, args));
        }

        @Test
        @DisplayName("Should throw BeansException when args types don't match")
        void shouldThrowExceptionWhenArgTypesDoNotMatch() throws Exception {
            // Arrange
            Constructor<ParameterizedBean> constructor = ParameterizedBean.class.getConstructor(String.class, int.class);
            Object[] args = new Object[]{"test", "not an integer"}; // Wrong type for second argument

            // Assert
            assertThrows(BeansException.class, 
                () -> instantiationStrategy.instance(ParameterizedBean.class, constructor, args));
        }
    }

    // Test classes

    static class TestBean {
        // Default constructor is implicitly defined
    }

    static class NoDefaultConstructorBean {
        @SuppressWarnings("unused")
        private final String value;

        public NoDefaultConstructorBean(String value) {
            this.value = value;
        }
    }

    static class ParameterizedBean {
        private final String name;
        private final int value;

        public ParameterizedBean(String name, int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }
    }
}