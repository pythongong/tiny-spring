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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pythongong.enums.ScopeEnum;
import com.pythongong.test.utils.TestUtils.TestBean;

/**
 * Test class for {@link BeanDefinition}.
 *
 * @author Cheng Gong
 */
@DisplayName("Bean Definition Tests")
class BeanDefinitionTest {

    @Test
    @DisplayName("Should create bean definition with builder")
    void shouldCreateBeanDefinitionWithBuilder() throws Exception {
        // Arrange
        Constructor<TestBean> constructor = TestBean.class.getConstructor();
        Method initMethod = TestBean.class.getMethod("setName", String.class);
        Method destroyMethod = TestBean.class.getMethod("setValue", int.class);

        // Act
        BeanDefinition definition = BeanDefinition.builder()
            .beanName("testBean")
            .beanClass(TestBean.class)
            .scope(ScopeEnum.SINGLETON)
            .constructor(constructor)
            .initMethod(initMethod)
            .destroyMethod(destroyMethod)
            .build();

        // Assert
        assertEquals("testBean", definition.beanName());
        assertEquals(TestBean.class, definition.beanClass());
        assertEquals(ScopeEnum.SINGLETON, definition.scope());
        assertEquals(constructor, definition.constructor());
        assertEquals(initMethod, definition.initMethod());
        assertEquals(destroyMethod, definition.destroyMethod());
    }

    @Test
    @DisplayName("Should create bean definition with minimum required fields")
    void shouldCreateBeanDefinitionWithMinimumFields() {
        // Act
        BeanDefinition definition = BeanDefinition.builder()
            .beanName("testBean")
            .beanClass(TestBean.class)
            .build();

        // Assert
        assertEquals("testBean", definition.beanName());
        assertEquals(TestBean.class, definition.beanClass());
        assertEquals(ScopeEnum.SINGLETON, definition.scope()); // Default scope
        assertNull(definition.constructor());
        assertNull(definition.initMethod());
        assertNull(definition.destroyMethod());
    }

    @Test
    @DisplayName("Should implement equals and hashCode correctly")
    void shouldImplementEqualsAndHashCodeCorrectly() {
        // Arrange
        BeanDefinition def1 = BeanDefinition.builder()
            .beanName("testBean")
            .beanClass(TestBean.class)
            .build();

        BeanDefinition def2 = BeanDefinition.builder()
            .beanName("testBean")
            .beanClass(TestBean.class)
            .build();

        BeanDefinition def3 = BeanDefinition.builder()
            .beanName("differentBean")
            .beanClass(TestBean.class)
            .build();

        // Assert
        assertEquals(def1, def2);
        assertEquals(def1.hashCode(), def2.hashCode());
        assertNotEquals(def1, def3);
        assertNotEquals(def1.hashCode(), def3.hashCode());
    }
}