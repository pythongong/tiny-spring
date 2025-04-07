/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pythongong.restful;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;

import com.pythongong.context.ApplicationContext;
import com.pythongong.context.impl.PropertyResolver;
import com.pythongong.utils.WebUtils;

import jakarta.servlet.ServletContext;

class ContextLoaderInitializeTest {

    @Mock
    private ServletContext servletContext;

    @Mock
    private PropertyResolver propertyResolver;

    private ContextLoaderInitialize initializer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        initializer = new ContextLoaderInitialize(TestConfig.class, propertyResolver);
    }

    @Test
    void testDefaultEncoding() throws Exception {
        try (MockedStatic<WebUtils> mockedWebUtils = mockStatic(WebUtils.class)) {
            mockedWebUtils.when(() -> WebUtils.resgisterDispatcher(any(ServletContext.class),
                    any(ApplicationContext.class)))
                    .thenAnswer(inv -> null);
            mockedWebUtils.when(() -> WebUtils.registerFilters(any(ServletContext.class),
                    any(ApplicationContext.class))).thenAnswer(inv -> null);

            when(propertyResolver.getProperty("${tiny-spring.web.character-encoding:UTF-8}"))
                    .thenReturn("UTF-8");

            initializer.onStartup(new HashSet<>(), servletContext);

            verify(servletContext).setRequestCharacterEncoding("UTF-8");
            verify(servletContext).setResponseCharacterEncoding("UTF-8");
        }

    }

    @Test
    void testCustomEncoding() throws Exception {
        try (MockedStatic<WebUtils> mockedWebUtils = mockStatic(WebUtils.class)) {
            mockedWebUtils.when(() -> WebUtils.resgisterDispatcher(any(ServletContext.class),
                    any(ApplicationContext.class)))
                    .thenAnswer(inv -> null);
            mockedWebUtils.when(() -> WebUtils.registerFilters(any(ServletContext.class),
                    any(ApplicationContext.class))).thenAnswer(inv -> null);
            when(propertyResolver.getProperty("${tiny-spring.web.character-encoding:UTF-8}"))
                    .thenReturn("GBK");

            initializer.onStartup(new HashSet<>(), servletContext);

            verify(servletContext).setRequestCharacterEncoding("GBK");
            verify(servletContext).setResponseCharacterEncoding("GBK");
        }
    }

    @Test
    void testServletContextConfiguration() throws Exception {
        try (MockedStatic<WebUtils> mockedWebUtils = mockStatic(WebUtils.class)) {
            mockedWebUtils.when(() -> WebUtils.resgisterDispatcher(any(ServletContext.class),
                    any(ApplicationContext.class)))
                    .thenAnswer(inv -> null);
            mockedWebUtils.when(() -> WebUtils.registerFilters(any(ServletContext.class),
                    any(ApplicationContext.class))).thenAnswer(inv -> null);
            when(propertyResolver.getProperty("${tiny-spring.web.character-encoding:UTF-8}"))
                    .thenReturn("UTF-8");

            initializer.onStartup(new HashSet<>(), servletContext);

            verify(servletContext, times(1)).setRequestCharacterEncoding(anyString());
            verify(servletContext, times(1)).setResponseCharacterEncoding(anyString());
        }
    }

    // Test configuration class
    private static class TestConfig {
        // Empty configuration class for testing
    }
}