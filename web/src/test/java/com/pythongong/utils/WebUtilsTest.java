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

package com.pythongong.utils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pythongong.context.impl.AnnotationConfigApplicationContext;
import com.pythongong.exception.WebException;
import com.pythongong.mock.jdbc.TestUser;
import com.pythongong.restful.DispatcherServlet;
import com.pythongong.restful.FilterRegistrationBean;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;

class WebUtilsTest {

    @Mock
    private ServletContext servletContext;

    @Mock
    private AnnotationConfigApplicationContext applicationContext;

    @Mock
    private Dynamic servletRegistration;

    @Mock
    private FilterRegistration.Dynamic filterRegistration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterDispatcher() {
        when(servletContext.addServlet(anyString(), any(DispatcherServlet.class))).thenReturn(servletRegistration);

        WebUtils.resgisterDispatcher(servletContext, applicationContext);

        verify(servletContext).addServlet(eq("dispatcherServlet"), any(DispatcherServlet.class));
        verify(servletRegistration).addMapping("/");
        verify(servletRegistration).setLoadOnStartup(0);
    }

    @Test
    void testGeneratePathPattern_SimpleUrl() {
        Pattern pattern = WebUtils.generatePathPattern("/users");
        assertTrue(pattern.matcher("/users").matches());
        assertFalse(pattern.matcher("/users/").matches());
    }

    @Test
    void testGeneratePathPattern_WithVariable() {
        Pattern pattern = WebUtils.generatePathPattern("/users/{id}/posts/{postId}");
        assertTrue(pattern.matcher("/users/123/posts/456").matches());
        assertFalse(pattern.matcher("/users/posts/456").matches());
    }

    @Test
    void testGeneratePathPattern_InvalidPattern() {
        assertThrows(WebException.class, () -> {
            WebUtils.generatePathPattern("/users/{id/posts");
        });
    }

    @Test
    void testRegisterFilters() {
        Filter filter = mock(Filter.class);
        FilterRegistrationBean mockBean = new FilterRegistrationBean(() -> Arrays.asList("/api/*"), () -> filter);

        Map<String, FilterRegistrationBean> beans = new HashMap<>();
        beans.put("testFilter", mockBean);

        when(applicationContext.getBeansOfType(FilterRegistrationBean.class)).thenReturn(beans);
        when(servletContext.addFilter(anyString(), any(Filter.class))).thenReturn(filterRegistration);

        WebUtils.registerFilters(servletContext, applicationContext);

        verify(servletContext).addFilter(anyString(), any(Filter.class));
        verify(filterRegistration).addMappingForUrlPatterns(any(), eq(true), any());
    }

    @Test
    void testRegisterForNullFilterRegis() {
        Filter filter = mock(Filter.class);
        FilterRegistrationBean mockBean = new FilterRegistrationBean(() -> Arrays.asList("/api/*"), () -> filter);

        Map<String, FilterRegistrationBean> beans = new HashMap<>();
        beans.put("testFilter", mockBean);

        when(applicationContext.getBeansOfType(FilterRegistrationBean.class)).thenReturn(beans);

        assertThrows(WebException.class, () -> WebUtils.registerFilters(servletContext, applicationContext));
    }

    @Test
    void testGetConfigPath() {

        String path = WebUtils.getConfigPath(TestUser.class);
        assertNotNull(path);
        System.out.println(path);
        assertTrue(path.equals("/D:/Projects/tiny-spring/web/target/test-classes/com/pythongong/mock/jdbc"));
    }

    @Test
    void testGetConfigPathWithNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            WebUtils.getConfigPath(null);
        });
    }
}