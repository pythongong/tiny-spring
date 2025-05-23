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
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.FieldValue;
import com.pythongong.beans.config.FieldValueList;
import com.pythongong.context.event.ApplicationEventMulticaster;
import com.pythongong.context.event.GeneralApplicationEventMulticaster;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.test.ioc.normal.AwareBean;
import com.pythongong.test.ioc.normal.BeanWithMethodInjection;
import com.pythongong.test.ioc.normal.BeanWithProperties;
import com.pythongong.test.ioc.normal.SubTestBean;
import com.pythongong.test.ioc.normal.TestBean;
import com.pythongong.test.ioc.normal.TestBeanPostProcessor;
import com.pythongong.test.ioc.normal.TestComponent;
import com.pythongong.test.ioc.normal.TestDisposableBean;
import com.pythongong.test.ioc.normal.TestInitializingBean;
import com.pythongong.util.ClassUtils;

/**
 * Unit tests for {@link DefaultListableBeanFactory}.
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
        private DefaultListableBeanFactory beanFactory;

        /**
         * Sets up the test environment before each test
         */
        @BeforeEach
        void setUp() {
                beanFactory = new DefaultListableBeanFactory();
        }

        /**
         * Tests the registration and retrieval of a bean definition
         * 
         * @throws SecurityException
         * @throws NoSuchMethodException
         */
        @Test
        @DisplayName("Should register and retrieve bean definition successfully")
        void shouldRegisterAndRetrieveBeanDefinition() throws NoSuchMethodException, SecurityException {
                // Given
                BeanDefinition beanDefinition = BeanDefinition.builder()
                                .beanName("testBean")
                                .beanClass(TestBean.class)
                                .constructor(TestBean.class.getConstructor())
                                .scope(ScopeEnum.SINGLETON)
                                .fieldValueList(new FieldValueList())
                                .build();

                // When
                beanFactory.registerBeanDefinition(beanDefinition);

                // Then
                BeanDefinition retrievedDefinition = beanFactory.getBeanDefinition(beanDefinition.beanName());
                assertNotNull(retrievedDefinition, "Retrieved bean definition should not be null");
                assertEquals(beanDefinition, retrievedDefinition,
                                "Retrieved bean definition should match the registered one");
        }

        /**
         * Tests the registration validation for null bean definition
         */
        @Test
        @DisplayName("Should throw exception when registering null bean definition")
        void shouldThrowExceptionWhenRegisteringNullBeanDefinition() {
                assertThrows(IllegalArgumentException.class,
                                () -> beanFactory.registerBeanDefinition(null),
                                "Should throw IllegalArgumentException for null bean definition");
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
         * Tests the pre-instantiation of singletons
         * 
         * @throws SecurityException
         * @throws NoSuchMethodException
         */
        @Test
        @DisplayName("Should pre-instantiate singletons")
        void shouldPreInstantiateSingletons() throws NoSuchMethodException, SecurityException {
                // Given
                BeanDefinition beanDefinition = BeanDefinition.builder()
                                .beanName("testBean")
                                .beanClass(TestBean.class)
                                .constructor(TestBean.class.getConstructor())
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
         * Tests for getBeansOfType method
         * 
         * @throws SecurityException
         * @throws NoSuchMethodException
         */
        @Test
        @DisplayName("Should get all beans of specified type")
        void shouldGetBeansOfSpecifiedType() throws NoSuchMethodException, SecurityException {
                // Given
                BeanDefinition beanDef1 = BeanDefinition.builder()
                                .beanName("testBean1")
                                .beanClass(TestBean.class)
                                .constructor(TestBean.class.getConstructor())
                                .build();

                BeanDefinition beanDef2 = BeanDefinition.builder()
                                .beanName("testBean2")
                                .constructor(TestBean.class.getConstructor())
                                .beanClass(TestBean.class)
                                .build();

                beanFactory.registerBeanDefinition(beanDef1);
                beanFactory.registerBeanDefinition(beanDef2);

                // When
                Map<String, TestBean> beans = beanFactory.getBeansOfType(TestBean.class);

                // Then
                assertNotNull(beans);
                assertEquals(2, beans.size());
                assertTrue(beans.containsKey("testBean1"));
                assertTrue(beans.containsKey("testBean2"));
        }

        @Test
        @DisplayName("Should throw exception when getting beans with null type")
        void shouldThrowExceptionWhenGettingBeansWithNullType() {
                assertThrows(IllegalArgumentException.class,
                                () -> beanFactory.getBeansOfType(null),
                                "Should throw IllegalArgumentException when type is null");
        }

        @Test
        @DisplayName("Should get beans of assignable type")
        void shouldGetBeansOfAssignableType() throws NoSuchMethodException, SecurityException {

                BeanDefinition beanDef = BeanDefinition.builder()
                                .beanName("subTestBean")
                                .beanClass(SubTestBean.class)
                                .constructor(SubTestBean.class.getConstructor())
                                .scope(ScopeEnum.SINGLETON)
                                .build();

                beanFactory.registerBeanDefinition(beanDef);

                // When
                Map<String, TestBean> beans = beanFactory.getBeansOfType(TestBean.class);

                // Then
                assertNotNull(beans);
                assertEquals(1, beans.size());
                assertTrue(beans.containsKey("subTestBean"));
                assertTrue(beans.get("subTestBean") instanceof SubTestBean);
        }

        @Test
        public void testDestroySingletons() throws NoSuchMethodException, SecurityException {

                // Create factory and register bean
                DefaultListableBeanFactory factory = new DefaultListableBeanFactory();

                // Register bean definition
                BeanDefinition bd = BeanDefinition.builder()
                                .beanName("testBean")
                                .beanClass(TestDisposableBean.class)
                                .constructor(TestDisposableBean.class.getConstructor())
                                .destroyMethodName("isDestroyed")
                                .build();
                factory.registerBeanDefinition(bd);

                // Verify bean is registered
                TestDisposableBean testBean = (TestDisposableBean) factory.getBean("testBean");
                assertNotNull(testBean);
                assertFalse(testBean.isDestroyed());

                // Call destroy singletons
                factory.destroySingletons();

                // Verify bean was destroyed
                // assertNull(factory.getSingleton("testBean"));
                assertTrue(testBean.isDestroyed());
        }

        @Test
        @DisplayName("Should create singleton bean with property values")
        void shouldCreateSingletonBeanWithPropertyValues() throws NoSuchMethodException, SecurityException {
                // Given
                String propertyValue = "test value";
                FieldValueList fieldValues = new FieldValueList();
                fieldValues.add(new FieldValue("testProperty", propertyValue));

                BeanDefinition beanDefinition = BeanDefinition.builder()
                                .beanName("beanWithProperties")
                                .beanClass(BeanWithProperties.class)
                                .constructor(BeanWithProperties.class.getConstructor())
                                .scope(ScopeEnum.SINGLETON)
                                .fieldValueList(fieldValues)
                                .build();

                // When
                beanFactory.registerBeanDefinition(beanDefinition);
                BeanWithProperties bean = (BeanWithProperties) beanFactory.getBean("beanWithProperties");

                // Then
                assertNotNull(bean);
                assertEquals(propertyValue, bean.getTestProperty());
        }

        @Test
        @DisplayName("Should handle bean initializing with initialization and destruction")
        void shouldHandleBeanInitializing() throws NoSuchMethodException, SecurityException {
                // Given
                BeanDefinition beanDefinition = BeanDefinition.builder()
                                .beanName("initializingBean")
                                .beanClass(TestInitializingBean.class)
                                .constructor(TestInitializingBean.class.getConstructor())
                                .initMethodName("afterPropertiesSet")
                                .scope(ScopeEnum.SINGLETON)
                                .build();

                // When
                beanFactory.registerBeanDefinition(beanDefinition);
                TestInitializingBean bean = (TestInitializingBean) beanFactory.getBean("initializingBean");

                // Then
                assertNotNull(bean);
                assertTrue(bean.isInitialized());
        }

        @Test
        @DisplayName("Should get bean definition by name and throw exception for empty name")
        void shouldGetBeanDefinitionAndHandleEmptyName() throws NoSuchMethodException, SecurityException {
                // Given
                BeanDefinition beanDefinition = BeanDefinition.builder()
                                .beanName("testBean")
                                .beanClass(TestBean.class)
                                .constructor(TestBean.class.getConstructor())
                                .build();
                beanFactory.registerBeanDefinition(beanDefinition);

                // When/Then
                // Should retrieve existing definition
                BeanDefinition retrieved = beanFactory.getBeanDefinition("testBean");
                assertNotNull(retrieved);
                assertEquals(beanDefinition, retrieved);

                // Should throw exception for empty name
                assertThrows(IllegalArgumentException.class,
                                () -> beanFactory.getBeanDefinition(""),
                                "Should throw exception for empty bean name");
        }

        @Test
        @DisplayName("Should handle aware interfaces correctly")
        void shouldHandleAwareInterfaces() throws NoSuchMethodException, SecurityException {

                BeanDefinition beanDefinition = BeanDefinition.builder()
                                .beanName("awareBean")
                                .beanClass(AwareBean.class)
                                .constructor(AwareBean.class.getConstructor())
                                .scope(ScopeEnum.SINGLETON)
                                .build();

                // When
                beanFactory.registerBeanDefinition(beanDefinition);
                AwareBean bean = (AwareBean) beanFactory.getBean("awareBean");

                // Then
                assertNotNull(bean);
                assertEquals("awareBean", bean.getBeanName());
                assertNotNull(bean.getBeanFactory());
                assertTrue(bean.getBeanFactory() instanceof DefaultListableBeanFactory);
        }

        @Test
        @DisplayName("Should handle prototype scope beans")
        void shouldHandlePrototypeScopeBeans() throws NoSuchMethodException, SecurityException {
                // Given
                BeanDefinition beanDefinition = BeanDefinition.builder()
                                .beanName("prototypeBean")
                                .beanClass(TestBean.class)
                                .constructor(TestBean.class.getConstructor())
                                .scope(ScopeEnum.PROTOTYPE)
                                .build();

                // When
                beanFactory.registerBeanDefinition(beanDefinition);
                Object bean1 = beanFactory.getBean("prototypeBean");
                Object bean2 = beanFactory.getBean("prototypeBean");

                // Then
                assertNotNull(bean1);
                assertNotNull(bean2);
                assertNotSame(bean1, bean2, "Prototype beans should be different instances");
        }

        @Test
        @DisplayName("Should inject dependencies correctly")
        void shouldInjectDependenciesCorrectly() throws NoSuchMethodException, SecurityException {
                // Given
                BeanDefinition dependencyDef = BeanDefinition.builder()
                                .beanName("dependency")
                                .beanClass(TestBean.class)
                                .constructor(TestBean.class.getConstructor())
                                .scope(ScopeEnum.SINGLETON)
                                .build();

                BeanDefinition mainBeanDef = BeanDefinition.builder()
                                .beanName("mainBean")
                                .beanClass(BeanWithMethodInjection.class)
                                .constructor(BeanWithMethodInjection.class.getConstructor())
                                .scope(ScopeEnum.SINGLETON)
                                .build();

                // When
                beanFactory.registerBeanDefinition(dependencyDef);
                beanFactory.registerBeanDefinition(mainBeanDef);

                BeanWithMethodInjection mainBean = (BeanWithMethodInjection) beanFactory.getBean("mainBean");
                TestBean dependency = (TestBean) beanFactory.getBean("dependency");

                // Then
                assertNotNull(mainBean);
                assertNotNull(mainBean.getTestBean());
                assertTrue(dependency == mainBean.getTestBean());
        }

        @Test
        void shouldBeanPostPorcessorWork() {
                BeanDefinition beanDefinition = BeanDefinition.builder()
                                .beanClass(TestComponent.class)
                                .beanName("testComponent")
                                .build();
                beanFactory.registerBeanDefinition(beanDefinition);
                beanFactory.addBeanProcessor(new TestBeanPostProcessor());
                TestComponent testComponent = beanFactory.getBean("testComponent", TestComponent.class);
                assertTrue(testComponent.isPostProcessed());
        }

        /**
         * Tests validation of null post processors.
         */
        @Test
        void whenAddNullPostProcessor_thenThrowsException() {
                assertThrows(IllegalArgumentException.class, () -> beanFactory.addBeanProcessor(null));
        }

}
