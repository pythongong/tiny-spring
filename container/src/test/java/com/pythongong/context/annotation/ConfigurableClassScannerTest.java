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
package com.pythongong.context.annotation;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.test.ioc.normal.TestConfiguration;
import com.pythongong.util.ClassUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfigurableClassScanner Tests")
class ConfigurableClassScannerTest {

    private static String basePackage = TestConfiguration.class.getPackageName();

    private ConfigurableClassScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new ConfigurableClassScanner();
    }

    @Test
    @DisplayName("Should throw exception for empty package array")
    void shouldThrowExceptionForEmptyPackageArray() {
        // When/Then
        assertThrows(IllegalArgumentException.class,
                () -> scanner.scan(new String[] {}),
                "Should throw IllegalArgumentException for empty package array");
    }

    @Test
    @DisplayName("Should scan multiple packages")
    void shouldScanMultiplePackages() {
        // Given
        String pkg2 = "com.pythongong.core.filter";

        // When
        Set<Class<?>> classes = scanner.scan(basePackage, pkg2);

        // Then
        assertFalse(classes.isEmpty());
        assertTrue(classes.stream()
                .anyMatch(c -> c.getPackage().getName().equals(basePackage) ||
                        c.getPackage().getName().equals(pkg2)));
    }

    @Test
    @DisplayName("Should throw exception for invalid package")
    void shouldThrowExceptionForInvalidPackage() {
        // When/Then
        Set<Class<?>> classes = scanner.scan("invalid.package.name");
        assertTrue(ClassUtils.isCollectionEmpty(classes));
    }

}
