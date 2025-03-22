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
import java.net.URLClassLoader;
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

import com.pythongong.test.utils.TestConfiguration;
import com.pythongong.exception.BeansException;
import com.pythongong.test.utils.FactoryPostProcessedBean;
import com.pythongong.test.utils.LifecycleTestBean;
import com.pythongong.test.utils.ProxyBeanFactory;
import com.pythongong.test.utils.TestComponent;
import com.pythongong.test.utils.TestPropertyComponent;
import com.pythongong.test.utils.TestUsingProxy;
import com.pythongong.util.ClassUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnnotationConfigApplicationContext Tests")
class AnnotationConfigApplicationContextTest {

    private static ClassLoader testClassLoader;

    private static Path propertiesFile;

    @BeforeAll
    static void setUp() throws IOException, URISyntaxException {
        testClassLoader = Thread.currentThread().getContextClassLoader();

        Path testClassesDir = Path.of(new URI(Objects.requireNonNull(testClassLoader.getResource(".")).toString()));
        ;
        propertiesFile = testClassesDir.resolve("test.properties");
        try (FileWriter writer = new FileWriter(propertiesFile.toFile())) {
            writer.write("test.name=testValue\n");
            writer.write("test.version=1.0.0\n");
            writer.write("app.description=Test Application\n");
        }
        // Verify file creation
        assertTrue(Files.exists(propertiesFile));
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
        // Close the test classloader
        if (testClassLoader instanceof URLClassLoader) {
            ((URLClassLoader) testClassLoader).close();
        }
    }

    @Test
    @DisplayName("Should create a bean")
    void shouldCreateBean() {
        // Given
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

        // When
        TestComponent component = context.getBean("testComponent", TestComponent.class);

        // Then
        assertNotNull(component);
    }

    @Test
    @DisplayName("Should create a bean")
    void shouldGetBeanByName() {
        // Given
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

        // When
        TestComponent component = (TestComponent) context.getBean("testComponent");

        // Then
        assertNotNull(component);
    }

    @Test
    @DisplayName("Should create a bean")
    void shouldGetBeansByType() {
        // Given
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

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
        // Given
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

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
        assertThrows(BeansException.class,
                () -> new AnnotationConfigApplicationContext(null));
    }

    @Test
    @DisplayName("Should register and apply BeanFactoryPostProcessor")
    void shouldRegisterAndApplyBeanFactoryPostProcessor() {
        // Given
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfiguration.class);

        // When
        FactoryPostProcessedBean bean = context.getBean("beanFactoryPostProcessedBean", FactoryPostProcessedBean.class);

        // Then
        assertNotNull(bean);
        assertEquals(FactoryPostProcessedBean.PROCESSED_NAME, bean.getName());
    }

    @Test
    @DisplayName("Should handle prxoxy bean")
    void shouldHandlePrxoyBean() {
        // Given
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(
                TestConfiguration.class);

        // When
        TestUsingProxy bean = context.getBean("testUsingProxy", TestUsingProxy.class);
        assertNotNull(bean);

        ProxyBeanFactory.data.forEach((id, name) -> {
            assertEquals(name, bean.getName(id));
        });

    }

}
