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
package com.pythongong.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.exception.BeansException;

/**
 * Unit tests for {@link PathUtils}.
 * Tests the file and classpath operations functionality.
 *
 * @author pythongong
 * @since 2025-03-21 00:37:45
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("PathUtils Tests")
class PathUtilsTest {
    
    private static final String TEST_RESOURCES_PATH = "pathutils_test";
    private static final List<Path> createdPaths = new ArrayList<>();
    private final List<String> foundFiles = new ArrayList<>();
    
    private static Path testResourcesRoot;
    private static ClassLoader testClassLoader;
    private MockedStatic<ClassUtils> mockedClassUtils;

    @BeforeAll
    static void setUp() throws IOException {
        // Create test directory structure in target/test-classes
        testResourcesRoot = Paths.get("target", "test-classes", TEST_RESOURCES_PATH);
        createTestDirectories();
        createTestFiles();
        
        // Create URLClassLoader with test resources
        URL testResourcesUrl = testResourcesRoot.toUri().toURL();
        testClassLoader = new URLClassLoader(new URL[]{testResourcesUrl}, null);
    }

    @BeforeEach
    void setUpEach() {
        foundFiles.clear();
        // Create fresh mock for each test
        mockedClassUtils = mockStatic(ClassUtils.class);
        mockedClassUtils.when(ClassUtils::getDefaultClassLoader).thenReturn(testClassLoader);
    }

    @AfterEach
    void tearDownEach() {
        // Close the mock after each test
        if (mockedClassUtils != null) {
            mockedClassUtils.close();
        }
    }

    @AfterAll
    static void tearDown() throws IOException {
        // Clean up created files and directories in reverse order
        for (int i = createdPaths.size() - 1; i >= 0; i--) {
            Files.deleteIfExists(createdPaths.get(i));
        }

        // Close the test classloader
        if (testClassLoader instanceof URLClassLoader) {
            ((URLClassLoader) testClassLoader).close();
        }
    }

    private static void createTestDirectories() throws IOException {
        createDirectory(testResourcesRoot);
        createDirectory(testResourcesRoot.resolve("subdir"));
    }

    private static void createTestFiles() throws IOException {
        // Create test files
        createFile(testResourcesRoot.resolve("test1.properties"), "test1.properties content");
        createFile(testResourcesRoot.resolve("test2.txt"), "test2.txt content");
        createFile(testResourcesRoot.resolve("subdir").resolve("test3.properties"), "test3.properties content");
        createFile(testResourcesRoot.resolve("test with spaces.txt"), "test with spaces content");
        createFile(testResourcesRoot.resolve("test#special$chars.txt"), "special chars content");
    }

    private static void createDirectory(Path dir) throws IOException {
        Files.createDirectories(dir);
        createdPaths.add(dir);
    }

    private static void createFile(Path file, String content) throws IOException {
        Files.writeString(file, content);
        createdPaths.add(file);
    }

    @Test
    @DisplayName("Should find files in classpath without subdirectories")
    void shouldFindFilesWithoutSubdirectories() throws IOException {
        // Given
        BiConsumer<Path, Path> pathMapper = (basePath, filePath) -> 
            foundFiles.add(filePath.getFileName().toString());
        
        ClassPathSerchParam param = ClassPathSerchParam.builder()
                .packagePath(PathUtils.ROOT_CLASS_PATH)
                .pathMapper(pathMapper)
                .searchSudDirect(false)
                .serachFile(true)
                .serachJar(false)
                .build();

        // When
        PathUtils.findClassPathFileNames(param);

        // Then
        assertEquals(4, foundFiles.size());
        assertTrue(foundFiles.contains("test1.properties"));
        assertTrue(foundFiles.contains("test2.txt"));
        assertTrue(foundFiles.contains("test with spaces.txt"));
        assertTrue(foundFiles.contains("test#special$chars.txt"));
    }

    @Test
    @DisplayName("Should find files including subdirectories")
    void shouldFindFilesWithSubdirectories() {
        // Given
        BiConsumer<Path, Path> pathMapper = (basePath, filePath) -> 
            foundFiles.add(filePath.getFileName().toString());

        ClassPathSerchParam param = ClassPathSerchParam.builder()
                .packagePath(PathUtils.ROOT_CLASS_PATH)
                .pathMapper(pathMapper)
                .searchSudDirect(true)
                .serachFile(true)
                .serachJar(false)
                .build();

        // When
        PathUtils.findClassPathFileNames(param);

        // Then
        assertEquals(5, foundFiles.size());
        assertTrue(foundFiles.contains("test3.properties"));
    }

    @Test
    @DisplayName("Should find only property files")
    void shouldFindOnlyPropertyFiles() {
        // Given
        BiConsumer<Path, Path> pathMapper = (basePath, filePath) -> {
            if (filePath.toString().endsWith(PathUtils.PROPERTY_SUFFIX)) {
                foundFiles.add(filePath.getFileName().toString());
            }
        };

        ClassPathSerchParam param = ClassPathSerchParam.builder()
                .packagePath(PathUtils.ROOT_CLASS_PATH)
                .pathMapper(pathMapper)
                .searchSudDirect(true)
                .serachFile(true)
                .serachJar(false)
                .build();

        // When
        PathUtils.findClassPathFileNames(param);

        // Then
        assertEquals(2, foundFiles.size());
        assertTrue(foundFiles.contains("test1.properties"));
        assertTrue(foundFiles.contains("test3.properties"));
    }

    @Test
    @DisplayName("Should handle non-existent classpath resource")
    void shouldHandleNonExistentClasspathResource() {
        // Given
        BiConsumer<Path, Path> pathMapper = (basePath, filePath) -> 
            foundFiles.add(filePath.getFileName().toString());

        ClassPathSerchParam param = ClassPathSerchParam.builder()
                .packagePath("non/existent/path")
                .pathMapper(pathMapper)
                .searchSudDirect(true)
                .serachFile(true)
                .serachJar(false)
                .build();

        // When
        PathUtils.findClassPathFileNames(param);

        // Then
        assertTrue(foundFiles.isEmpty());
    }

    @Test
    @DisplayName("Should throw exception when search parameter is null")
    void shouldThrowExceptionWhenParamIsNull() {
        assertThrows(BeansException.class, 
            () -> PathUtils.findClassPathFileNames(null),
            "Should throw BeansException when param is null");
    }

    @Test
    @DisplayName("Should not search when both file and jar search are disabled")
    void shouldNotSearchWhenBothSearchesDisabled() {
        // Given
        BiConsumer<Path, Path> pathMapper = (basePath, filePath) -> 
            foundFiles.add(filePath.getFileName().toString());

        ClassPathSerchParam param = ClassPathSerchParam.builder()
                .packagePath(PathUtils.ROOT_CLASS_PATH)
                .pathMapper(pathMapper)
                .searchSudDirect(true)
                .serachFile(false)
                .serachJar(false)
                .build();

        // When
        PathUtils.findClassPathFileNames(param);

        // Then
        assertTrue(foundFiles.isEmpty());
    }

    @Test
    @DisplayName("Should convert package name to path")
    void shouldConvertPackageToPath() {
        // Given
        String packageName = "com.pythongong.util";
        String expectedPath = "com/pythongong/util";

        // When
        String result = PathUtils.convertPackageToPath(packageName);

        // Then
        assertEquals(expectedPath, result);
    }

    @Test
    @DisplayName("Should maintain forward slashes across platforms")
    void shouldMaintainForwardSlashes() {
        // Given
        String packageName = "com.test.package";
        
        // When
        String result = PathUtils.convertPackageToPath(packageName);

        // Then
        assertFalse(result.contains("\\"), 
            "Should not contain Windows-style backslashes");
        assertTrue(result.contains("/"), 
            "Should contain forward slashes");
    }

    @Test
    @DisplayName("Should handle null classloader gracefully")
    void shouldHandleNullClassloader() {
        // Given
        mockedClassUtils.when(ClassUtils::getDefaultClassLoader).thenReturn(null);
        
        BiConsumer<Path, Path> pathMapper = (basePath, filePath) -> 
            foundFiles.add(filePath.getFileName().toString());

        ClassPathSerchParam param = ClassPathSerchParam.builder()
                .packagePath(PathUtils.ROOT_CLASS_PATH)
                .pathMapper(pathMapper)
                .searchSudDirect(true)
                .serachFile(true)
                .serachJar(true)
                .build();

        // When/Then
        assertThrows(BeansException.class, () -> PathUtils.findClassPathFileNames(param));
    }
    
}