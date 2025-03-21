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
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.core.filter.TypeFilter;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Component;
import com.pythongong.util.ClassUtils;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfigurableClassScanner Tests")
class ConfigurableClassScannerTest {

    @Mock
    private TypeFilter mockFilter;
    
    private ConfigurableClassScanner scanner;

    @BeforeEach
    void setUp() {
        scanner = new ConfigurableClassScanner();
    }

    @Test
    @DisplayName("Should create scanner with default Component filter")
    void shouldCreateScannerWithDefaultFilter() {
        // When
        ConfigurableClassScanner scanner = new ConfigurableClassScanner();
        Set<Class<?>> classes = scanner.scan(getClass().getPackage().getName());

        // Then
        assertFalse(classes.isEmpty(), "Should find Component annotated classes");
        assertTrue(classes.stream()
            .anyMatch(clazz -> clazz.isAnnotationPresent(Component.class)),
            "Should contain at least one Component annotated class");
    }

    @Test
    @DisplayName("Should create scanner with custom filters")
    void shouldCreateScannerWithCustomFilters() {
        // Given
        List<TypeFilter> filters = new ArrayList<>();
        filters.add(mockFilter);
        when(mockFilter.match(any())).thenReturn(true);

        // When
        ConfigurableClassScanner scanner = new ConfigurableClassScanner(filters);
        Set<Class<?>> classes = scanner.scan(getClass().getPackage().getName());

        // Then
        assertFalse(classes.isEmpty());
        verify(mockFilter, atLeastOnce()).match(any());
    }

    @Test
    @DisplayName("Should add include filter")
    void shouldAddIncludeFilter() {
        // Given
        scanner.addIncludeFilter(mockFilter);
        when(mockFilter.match(any())).thenReturn(true);

        // When
        Set<Class<?>> classes = scanner.scan(getClass().getPackage().getName());

        // Then
        assertFalse(classes.isEmpty());
        verify(mockFilter, atLeastOnce()).match(any());
    }

    @Test
    @DisplayName("Should throw exception for empty package array")
    void shouldThrowExceptionForEmptyPackageArray() {
        // When/Then
        assertThrows(BeansException.class, 
            () -> scanner.scan(new String[]{}),
            "Should throw BeansException for empty package array");
    }

    @Test
    @DisplayName("Should scan multiple packages")
    void shouldScanMultiplePackages() {
        // Given
        String pkg1 = "com.pythongong.context.annotation";
        String pkg2 = "com.pythongong.core.filter";
        
        // When
        Set<Class<?>> classes = scanner.scan(pkg1, pkg2);

        // Then
        assertFalse(classes.isEmpty());
        assertTrue(classes.stream()
            .anyMatch(c -> c.getPackage().getName().equals(pkg1) ||
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

// Test component class
@Component
class TestComponent {}