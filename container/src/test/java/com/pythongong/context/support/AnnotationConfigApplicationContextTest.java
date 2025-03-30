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
package com.pythongong.context.support;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.test.aop.valid.AdviceOrder;
import com.pythongong.test.aop.valid.AopConfig;
import com.pythongong.test.aop.valid.AopTestTarget;
import com.pythongong.test.ioc.normal.ContextRefreshListener;
import com.pythongong.test.ioc.normal.FactoryPostProcessedBean;
import com.pythongong.test.ioc.normal.LifecycleTestBean;
import com.pythongong.test.ioc.normal.ProxyBeanFactory;
import com.pythongong.test.ioc.normal.TestComponent;
import com.pythongong.test.ioc.normal.TestConfiguration;
import com.pythongong.test.ioc.normal.TestPropertyComponent;
import com.pythongong.test.ioc.normal.TestUsingProxy;
import com.pythongong.util.ClassUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnnotationConfigApplicationContext Tests")
class AnnotationConfigApplicationContextTest {

    private static ClassLoader testClassLoader;

    private static Path propertiesFile;

    private static AnnotationConfigApplicationContext context;

    @BeforeAll
    static void setUp() throws IOException, URISyntaxException {
        testClassLoader = Thread.currentThread().getContextClassLoader();

        Path testClassesDir = Path.of(new URI(Objects.requireNonNull(testClassLoader.getResource(".")).toString()));

        propertiesFile = testClassesDir.resolve("test.properties");
        try (FileWriter writer = new FileWriter(propertiesFile.toFile())) {
            writer.write("test.name=testValue\n");
            writer.write("test.version=1.0.0\n");
            writer.write("app.description=Test Application\n");
        }
        // Verify file creation
        assertTrue(Files.exists(propertiesFile));
        context = new AnnotationConfigApplicationContext(
                TestConfiguration.class);
    }

    @BeforeEach
    void setUpEach() throws IOException {
        try (MockedStatic<ClassUtils> mockedClassUtils = mockStatic(ClassUtils.class, Mockito.CALLS_REAL_METHODS)) {
            mockedClassUtils.when(() -> ClassUtils.getDefaultClassLoader()).thenReturn(testClassLoader);
        }

    }

    @AfterAll
    static void tearDown() throws IOException {
        // Clean up the properties file
        Files.deleteIfExists(propertiesFile);
    }

    @Test
    @DisplayName("Should create a bean")
    void shouldCreateBean() {

        // When
        TestComponent component = context.getBean("testComponent", TestComponent.class);

        // Then
        assertNotNull(component);
    }

    @Test
    @DisplayName("Should create a bean")
    void shouldGetBeanByName() {

        // When
        TestComponent component = (TestComponent) context.getBean("testComponent");

        // Then
        assertNotNull(component);
    }

    @Test
    @DisplayName("Should create a bean")
    void shouldGetBeansByType() {

        // When
        Map<String, TestComponent> beans = context.getBeansOfType(TestComponent.class);

        // Then
        assertFalse(beans == null || beans.isEmpty());
        assertTrue(beans.containsKey("testComponent"));
    }

    @Test
    @DisplayName("Should load properties from classpath")
    void shouldLoadPropertiesFromClasspath() throws IOException {
        // When
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

        // Then
        TestPropertyComponent bean = context.getBean("testPropertyComponent", TestPropertyComponent.class);
        assertEquals("testValue", bean.getName(), "Should inject property value");
        assertEquals("1.0.0", bean.getVersion(), "Should inject version property");
        assertEquals("Test Application", bean.getDescription(), "Should inject description property");

    }

    @Test
    @DisplayName("Should register and apply BeanPostProcessor")
    void shouldRegisterAndApplyBeanPostProcessor() {

        // When
        TestComponent component = context.getBean("testComponent", TestComponent.class);

        // Then
        assertTrue(component.isPostProcessed(),
                "Bean should be post-processed");
    }

    @Test
    @DisplayName("Should handle bean lifecycle")
    void shouldHandleBeanLifecycle() {
        // Given
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                TestConfiguration.class);
        context.registerShutdownHook();

        // When
        LifecycleTestBean bean = context.getBean("lifecycleBean", LifecycleTestBean.class);

        // Then
        assertTrue(bean.isInitialized(), "Bean should be initialized");

        // When
        context.close();

        // Then
        assertTrue(bean.isDestroyed(), "Bean should be destroyed");
    }

    @Test
    @DisplayName("Should throw exception when configuration class is null")
    void shouldThrowExceptionWhenConfigurationClassIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> new AnnotationConfigApplicationContext(null));
    }

    @Test
    @DisplayName("Should register and apply BeanFactoryPostProcessor")
    void shouldRegisterAndApplyBeanFactoryPostProcessor() {

        // When
        FactoryPostProcessedBean bean = context.getBean("beanFactoryPostProcessedBean", FactoryPostProcessedBean.class);

        // Then
        assertNotNull(bean);
        assertEquals(FactoryPostProcessedBean.PROCESSED_NAME, bean.getName());
    }

    @Test
    @DisplayName("Should handle prxoxy bean")
    void shouldHandlePrxoyBean() {

        // When
        TestUsingProxy bean = context.getBean("testUsingProxy", TestUsingProxy.class);
        assertNotNull(bean);

        ProxyBeanFactory.data.forEach((id, name) -> {
            assertEquals(name, bean.getName(id));
        });

    }

    @Test
    @DisplayName("Should publish refresh event")
    void shouldPublishRefreshEvent() {
        // Given

        // When
        ContextRefreshListener listener = context.getBean("contextRefreshListener", ContextRefreshListener.class);

        // Then
        assertTrue(listener.isRefreshed(), "Context should be refreshed");
    }

    @Test
    void shouldCreateAop() {

        // Given
        AnnotationConfigApplicationContext aopContext = new AnnotationConfigApplicationContext(
                AopConfig.class);
        Object target = aopContext.getBean("aopTestTarget");
        assertNotNull(target);
        assertTrue(target instanceof AopTestTarget);
        ((AopTestTarget) target).getProxy();
        assertFalse(AdviceOrder.ORDER.isEmpty());
    }

}
