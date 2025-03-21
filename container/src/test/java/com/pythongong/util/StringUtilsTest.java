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

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Unit tests for {@link StringUtils}.
 *
 * @author Cheng Gong
 * @since 2025-03-20 11:25:29
 */
@DisplayName("StringUtils Tests")
class StringUtilsTest {

    @Test
    @DisplayName("Should return true for null string")
    void shouldReturnTrueForNullString() {
        assertTrue(StringUtils.isEmpty(null));
    }

    @Test
    @DisplayName("Should return true for empty string")
    void shouldReturnTrueForEmptyString() {
        assertTrue(StringUtils.isEmpty(""));
    }

    @ParameterizedTest
    @DisplayName("Should return true for strings with only whitespace")
    @ValueSource(strings = {" ", "   ", "\t", "\n", "\r", " \t\n\r"})
    void shouldReturnTrueForWhitespaceStrings(String input) {
        assertTrue(StringUtils.isEmpty(input));
    }

    @Test
    @DisplayName("Should return false for string with non-whitespace characters")
    void shouldReturnFalseForNonEmptyString() {
        assertFalse(StringUtils.isEmpty("test"));
    }

    @ParameterizedTest
    @DisplayName("Should return false for strings with content and whitespace")
    @ValueSource(strings = {" a ", " test ", "\tvalue\n", "multiple words"})
    void shouldReturnFalseForStringsWithContentAndWhitespace(String input) {
        assertFalse(StringUtils.isEmpty(input));
    }

    @Test
    @DisplayName("Should return false for string with only numbers")
    void shouldReturnFalseForStringWithOnlyNumbers() {
        assertFalse(StringUtils.isEmpty("123"));
    }

    @Test
    @DisplayName("Should return false for string with special characters")
    void shouldReturnFalseForStringWithSpecialCharacters() {
        assertFalse(StringUtils.isEmpty("!@#$"));
    }

    // ... other test methods remain the same ...

    @Test
    @DisplayName("Should find JUnit Test interface in classpath from JAR")
    void shouldFindJUnitTestInterfaceInClasspath() {
        // Given
        List<String> foundClasses = new ArrayList<>();
        BiConsumer<Path, Path> pathMapper = (basePath, filePath) -> {
            String fileName = filePath.getFileName().toString();
            if (fileName.endsWith(".class")) {
                foundClasses.add(fileName);
            }
        };

        // Get package path for org.junit.jupiter.api.Test
        String packagePath = Test.class.getPackage().getName().replace('.', '/');
        
        ClassPathSerchParam param = ClassPathSerchParam.builder()
                .packagePath(packagePath)
                .pathMapper(pathMapper)
                .searchSudDirect(false)
                .serachFile(false)
                .serachJar(true)
                .build();

        // When
        PathUtils.findClassPathFileNames(param);

        // Then
        assertFalse(foundClasses.isEmpty(), "Should find classes in JUnit package");
        assertTrue(
            foundClasses.contains("Test.class"),
            "Should find Test.class interface from JUnit"
        );
    }
}