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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.test.utils.TestUtils.TestBean;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Test class for {@link BeanDefinition} which is an immutable record holding
 * bean metadata including class, scope, initialization methods, and property values.
 *
 * @author Cheng Gong
 */
@DisplayName("BeanDefinition Tests")
class BeanDefinitionTest {

    @Nested
    @DisplayName("Construction Tests")
    class ConstructionTests {
        
        @Test
        @DisplayName("Should create bean definition with all required properties")
        void shouldCreateBeanDefinitionWithAllProperties() throws Exception {
            // Arrange
            Constructor<TestBean> constructor = TestBean.class.getConstructor();
            Method initMethod = TestBean.class.getMethod("init");
            Method destroyMethod = TestBean.class.getMethod("destroy");
            FieldValueList fieldValues = new FieldValueList();
            fieldValues.add(new FieldValue("name", "testValue"));

            // Act
            BeanDefinition definition = BeanDefinition.builder()
                .beanName("testBean")
                .beanClass(TestBean.class)
                .scope(ScopeEnum.SINGLETON)
                .constructor(constructor)
                .initMethod(initMethod)
                .destroyMethod(destroyMethod)
                .fieldValueList(fieldValues)
                .build();

            // Assert
            assertAll(
                () -> assertEquals("testBean", definition.beanName()),
                () -> assertEquals(TestBean.class, definition.beanClass()),
                () -> assertEquals(ScopeEnum.SINGLETON, definition.scope()),
                () -> assertEquals(constructor, definition.constructor()),
                () -> assertEquals(initMethod, definition.initMethod()),
                () -> assertEquals(destroyMethod, definition.destroyMethod()),
                () -> assertNotNull(definition.fieldValueList()),
                () -> assertEquals(1, definition.fieldValueList().size())
                
            );
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
            assertAll(
                () -> assertEquals("testBean", definition.beanName()),
                () -> assertEquals(TestBean.class, definition.beanClass()),
                () -> assertEquals(ScopeEnum.SINGLETON, definition.scope()),
                () -> assertNull(definition.constructor()),
                () -> assertNull(definition.initMethod()),
                () -> assertNull(definition.destroyMethod()),
                () -> assertNotNull(definition.fieldValueList()),
                () -> assertTrue(definition.fieldValueList().isEmpty())
            );
        }
    }

    @Nested
    @DisplayName("Default Value Tests")
    class DefaultValueTests {

        @Test
        @DisplayName("Should use default scope when scope is null")
        void shouldUseDefaultScopeWhenNull() {
            // Act
            BeanDefinition definition = BeanDefinition.builder()
                .beanName("testBean")
                .beanClass(TestBean.class)
                .scope(null)
                .build();

            // Assert
            assertEquals(ScopeEnum.SINGLETON, definition.scope());
        }

        @Test
        @DisplayName("Should create empty field value list when null")
        void shouldCreateEmptyFieldValueListWhenNull() {
            // Act
            BeanDefinition definition = BeanDefinition.builder()
                .beanName("testBean")
                .beanClass(TestBean.class)
                .fieldValueList(null)
                .build();

            // Assert
            assertAll(
                () -> assertNotNull(definition.fieldValueList()),
                () -> assertTrue(definition.fieldValueList().isEmpty())
            );
        }
    }

    @Nested
    @DisplayName("Field Value List Tests")
    class FieldValueListTests {

        @Test
        @DisplayName("Should maintain field value list after construction")
        void shouldMaintainFieldValueList() {
            // Arrange
            FieldValueList fieldValues = new FieldValueList();
            fieldValues.add(new FieldValue("name", "testValue"));
            fieldValues.add(new FieldValue("age", 25));

            // Act
            BeanDefinition definition = BeanDefinition.builder()
                .beanName("testBean")
                .beanClass(TestBean.class)
                .fieldValueList(fieldValues)
                .build();

            // Assert
            assertAll(
                () -> assertEquals(2, definition.fieldValueList().size())
                
            );
        }
    }

    @Nested
    @DisplayName("Equals and HashCode Tests")
    class EqualsAndHashCodeTests {

        @Test
        @DisplayName("Should implement equals correctly")
        void shouldImplementEqualsCorrectly() {
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
            assertAll(
                () -> assertEquals(def1, def1),                // Same object
                () -> assertEquals(def1, def2),                // Equal objects
                () -> assertNotEquals(def1, def3),             // Different objects
                () -> assertNotEquals(def1, null),             // Null comparison
                () -> assertNotEquals(def1, new Object())      // Different types
            );
        }

        @Test
        @DisplayName("Should implement hashCode consistently")
        void shouldImplementHashCodeConsistently() {
            // Arrange
            BeanDefinition def1 = createTestBeanDefinition("testBean");
            BeanDefinition def2 = createTestBeanDefinition("testBean");

            // Assert
            assertAll(
                () -> assertEquals(def1.hashCode(), def2.hashCode()),
                () -> assertEquals(def1.hashCode(), def1.hashCode()) // Multiple calls
            );
        }

        private BeanDefinition createTestBeanDefinition(String beanName) {
            return BeanDefinition.builder()
                .beanName(beanName)
                .beanClass(TestBean.class)
                .build();
        }
    }

    @Nested
    @DisplayName("Prototype Scope Tests")
    class PrototypeScopeTests {

        @Test
        @DisplayName("Should support prototype scope")
        void shouldSupportPrototypeScope() {
            // Act
            BeanDefinition definition = BeanDefinition.builder()
                .beanName("prototypeBean")
                .beanClass(TestBean.class)
                .scope(ScopeEnum.PROTOTYPE)
                .build();

            // Assert
            assertEquals(ScopeEnum.PROTOTYPE, definition.scope());
        }
    }
}