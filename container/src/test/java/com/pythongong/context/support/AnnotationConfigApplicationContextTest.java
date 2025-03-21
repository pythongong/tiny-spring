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
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.context.support.test_beans.PropertyTestConfiguration;
import com.pythongong.context.support.test_beans.TestPropertyComponent;
import com.pythongong.util.ClassUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("AnnotationConfigApplicationContext Tests")
class AnnotationConfigApplicationContextTest {
    
    @TempDir
    Path tempDir;

    private static final String TEST_RESOURCES_PATH = "com/pythongong/context/support";

    private static ClassLoader testClassLoader;
    
    private Path propertiesFile;

    // private MockedStatic<ClassUtils> mockedClassUtils;

    @BeforeAll
    static void setUp() throws IOException {
         // Create test directory structure in target/test-classes
        Path testResourcesRoot = Paths.get("target", "test-classes", TEST_RESOURCES_PATH);
        // Create URLClassLoader with test resources
        URL testResourcesUrl = testResourcesRoot.toUri().toURL();
        testClassLoader = new URLClassLoader(new URL[]{testResourcesUrl}, null);
    }

    @BeforeEach
    void setUpEach() throws IOException {
        // Create test.properties file
        // Create test directory structure in target/test-classes
        Path testResourcesRoot = Paths.get("target", "test-classes");
        propertiesFile = testResourcesRoot.resolve("test.properties");
        try (FileWriter writer = new FileWriter(propertiesFile.toFile())) {
            writer.write("test.name=testValue\n");
            writer.write("test.version=1.0.0\n");
            writer.write("app.description=Test Application\n");
        }

        
    
    }

    @AfterEach
    void tearDownEach() throws IOException {
        // Clean up the properties file
        Files.deleteIfExists(propertiesFile);
    }

    @AfterAll
    static void tearDown() throws IOException {
        // Close the test classloader
        if (testClassLoader instanceof URLClassLoader) {
            ((URLClassLoader) testClassLoader).close();
        }
    }

    @Test
    @DisplayName("Should load properties from classpath")
    void shouldLoadPropertiesFromClasspath() throws IOException {
        try (MockedStatic<ClassUtils> mockedClassUtils = mockStatic(ClassUtils.class, Mockito.CALLS_REAL_METHODS)) {
            // Given
            System.setProperty("java.class.path", tempDir.toString());
            
            // When
            AnnotationConfigApplicationContext context = 
                new AnnotationConfigApplicationContext(PropertyTestConfiguration.class);
            
            // Then
            TestPropertyComponent bean = context.getBean("testPropertyComponent", TestPropertyComponent.class);
            assertEquals("testValue", bean.getName(), "Should inject property value");
            assertEquals("1.0.0", bean.getVersion(), "Should inject version property");
            assertEquals("Test Application", bean.getDescription(), "Should inject description property");              
        }
        
        
    }
    
    
}
