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
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.pythongong.util.FileUtils;

@DisplayName("PropertyResolver Tests")
class PropertyResolverTest {

    private static Path propertiesFile;
    private static PropertyResolver resolver;

    @BeforeAll
    static void setUp() throws IOException, URISyntaxException {
        ClassLoader testClassLoader = Thread.currentThread().getContextClassLoader();

        Path testClassesDir = Path.of(new URI(Objects.requireNonNull(testClassLoader.getResource(".")).toString()));

        propertiesFile = testClassesDir.resolve("test.properties");
        try (FileWriter writer = new FileWriter(propertiesFile.toFile())) {
            writer.write("test.name=pythongong\n");
            writer.write("test.date=2025-03-21\n");
            writer.write("test.time=02:42:18\n");
            writer.write("app.version=${version:1.0.0}\n");
            writer.write("app.profile=${profile:dev}\n");
        }
        // Verify file creation
        assertTrue(Files.exists(propertiesFile));
        resolver = new PropertyResolver();
    }

    @AfterAll
    static void tearDown() throws IOException {
        // Clean up the properties file
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
        // assumeTrue(javaHome != null, "JAVA_HOME environment variable is required for
        // this test");

        // When/Then
        assertEquals(javaHome, resolver.getProperty("JAVA_HOME"),
                "Should resolve system environment variable");
    }

    @Test
    void shouldLoadYamlProperties() {
        String yamlDir = FileUtils.CLASSPATH_URL_PREFIX + "application.yml";

        Map<String, Object> yaml = FileUtils.loadYaml(yamlDir);
        resolver.addAll(yaml);
        assertNotNull(resolver.getProperty("${spring.datasource.auto-commit}", boolean.class));
    }
}