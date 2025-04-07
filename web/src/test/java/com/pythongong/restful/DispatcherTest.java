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
import static org.mockito.Mockito.*;

import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pythongong.exception.WebException;
import com.pythongong.mock.jdbc.TestUser;
import com.pythongong.mock.restful.TestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class DispatcherTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private TestController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller = new TestController();
    }

    @Test
    void testPathVariableHandling() throws Exception {
        Method method = TestController.class.getMethod("getUser", String.class);
        Dispatcher dispatcher = new Dispatcher(controller, method, "/users/{id}");

        when(request.getRequestURI()).thenReturn("/users/123");

        Result result = dispatcher.process(request, response);

        assertTrue(result.isProcessed());
        assertEquals("User:123", result.retVal());
    }

    @Test
    void testRequestParamHandling() throws Exception {
        Method method = TestController.class.getMethod("searchUsers", String.class);
        Dispatcher dispatcher = new Dispatcher(controller, method, "/users/search");

        when(request.getRequestURI()).thenReturn("/users/search");
        when(request.getParameter("name")).thenReturn("John");

        Result result = dispatcher.process(request, response);

        assertTrue(result.isProcessed());
        assertEquals("Search:John", result.retVal());
    }

    @Test
    void testRequestBodyHandling() throws Exception {
        Method method = TestController.class.getMethod("createUser", TestUser.class);
        Dispatcher dispatcher = new Dispatcher(controller, method, "/users");

        when(request.getRequestURI()).thenReturn("/users");
        when(request.getReader()).thenReturn(
                new BufferedReader(new StringReader("{\"name\":\"John\",\"age\":30}")));

        Result result = dispatcher.process(request, response);

        assertTrue(result.isProcessed());
        assertEquals("Created:John", result.retVal());
    }

    @Test
    void testUrlNotMatching() throws Exception {
        Method method = TestController.class.getMethod("getUser", String.class);
        Dispatcher dispatcher = new Dispatcher(controller, method, "/users/{id}");

        when(request.getRequestURI()).thenReturn("/posts/123");

        Result result = dispatcher.process(request, response);

        assertFalse(result.isProcessed());
        assertNull(result.retVal());
    }

    @Test
    void testInvalidParameterAnnotation() {
        Method method;
        try {
            method = TestController.class.getMethod("invalidParam", String.class);
            assertThrows(WebException.class, () -> new Dispatcher(controller, method, "/invalid"));
        } catch (NoSuchMethodException e) {
            fail("Test method not found");
        }
    }

}