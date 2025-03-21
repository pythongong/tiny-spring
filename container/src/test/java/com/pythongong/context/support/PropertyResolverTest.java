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

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

@DisplayName("PropertyResolver Tests")
class PropertyResolverTest {
    
    @TempDir
    Path tempDir;
    
    private Path propertiesFile;
    private PropertyResolver resolver;

    @BeforeEach
    void setUp() throws IOException {
        // Create test.properties file
        propertiesFile = tempDir.resolve("application.properties");
        try (FileWriter writer = new FileWriter(propertiesFile.toFile())) {
            writer.write("test.name=pythongong\n");
            writer.write("test.date=2025-03-21\n");
            writer.write("test.time=02:42:18\n");
            writer.write("app.version=${version:1.0.0}\n");
            writer.write("app.profile=${profile:dev}\n");
        }
        
        // Set up classpath to include our temp directory
        System.setProperty("java.class.path", tempDir.toString());
        
        // Create resolver with empty properties
        resolver = new PropertyResolver(new Properties());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(propertiesFile);
    }

    @Test
    @DisplayName("Should load and resolve properties")
    void shouldLoadAndResolveProperties() throws IOException {
        // When
        resolver.load(Files.newInputStream(propertiesFile));

        // Then
        assertEquals("pythongong", resolver.getProperty("test.name"), 
            "Should resolve simple property");
        assertEquals("2025-03-21", resolver.getProperty("test.date"), 
            "Should resolve date property");
        assertEquals("02:42:18", resolver.getProperty("test.time"), 
            "Should resolve time property");
    }

    @Test
    @DisplayName("Should resolve property with default value")
    void shouldResolvePropertyWithDefaultValue() throws IOException {
        // When
        resolver.load(Files.newInputStream(propertiesFile));

        // Then
        assertEquals("1.0.0", resolver.getProperty("${version:1.0.0}"), 
            "Should use default value when property is not defined");
        assertEquals("dev", resolver.getProperty("${profile:dev}"), 
            "Should use default value from properties file");
    }

    @Test
    @DisplayName("Should throw exception for missing property")
    void shouldThrowExceptionForMissingProperty() throws IOException {
        // When
        resolver.load(Files.newInputStream(propertiesFile));

        // Then
        assertThrows(NoSuchElementException.class, 
            () -> resolver.getProperty("non.existent.property"),
            "Should throw NoSuchElementException for missing property");
    }

    @Test
    @DisplayName("Should use system environment variables")
    void shouldUseSystemEnvironmentVariables() {
        // Given
        String javaHome = System.getenv("JAVA_HOME");
        // assumeTrue(javaHome != null, "JAVA_HOME environment variable is required for this test");

        // When/Then
        assertEquals(javaHome, resolver.getProperty("JAVA_HOME"),
            "Should resolve system environment variable");
    }
}