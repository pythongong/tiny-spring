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

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

class FilterRegistrationBeanTest {

    @Test
    void testValidConstruction() {
        TestFilter filter = new TestFilter();
        List<String> urlPatterns = Arrays.asList("/test/*", "/api/*");

        FilterRegistrationBean bean = new FilterRegistrationBean(
                () -> urlPatterns,
                () -> filter);

        assertEquals("TestFilter", bean.getName());
        assertSame(filter, bean.getFilter().get());
        assertSame(urlPatterns, bean.getUrlPatterns().get());
    }

    @Test
    void testNullFilter() {
        List<String> urlPatterns = Arrays.asList("/test/*");

        assertThrows(IllegalArgumentException.class, () -> {
            new FilterRegistrationBean(
                    () -> urlPatterns,
                    null);
        });
    }

    @Test
    void testNullUrlPatterns() {
        TestFilter filter = new TestFilter();

        assertThrows(IllegalArgumentException.class, () -> {
            new FilterRegistrationBean(
                    null,
                    () -> filter);
        });
    }

    @Test
    void testNullFilterSupplier() {

        FilterRegistrationBean bean = new FilterRegistrationBean(
                () -> Arrays.asList("/test/*"),
                () -> null);

        assertEquals("", bean.getName());
    }

    @Test
    void testEmptyUrlPatterns() {
        TestFilter filter = new TestFilter();

        FilterRegistrationBean bean = new FilterRegistrationBean(
                () -> Arrays.asList(),
                () -> filter);

        assertTrue(bean.getUrlPatterns().get().isEmpty());
    }

    // Test Filter implementation
    private static class TestFilter implements Filter {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
                throws IOException, ServletException {
            // Test implementation
        }
    }
}