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

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.pythongong.context.impl.AnnotationConfigApplicationContext;
import com.pythongong.mock.restful.TestController;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

class DispatcherServletTest {

    @Mock
    private AnnotationConfigApplicationContext applicationContext;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    private DispatcherServlet dispatcherServlet;
    private StringWriter stringWriter;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        // Setup response writer
        stringWriter = new StringWriter();
        writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Create test controller beans
        Map<String, Object> beans = new HashMap<>();
        beans.put("testController", new TestController());
        when(applicationContext.getBeansOfType(Object.class)).thenReturn(beans);

        dispatcherServlet = new DispatcherServlet(applicationContext);
        dispatcherServlet.init();
    }

    @Test
    void testHandleGetRequest() throws Exception {
        when(request.getRequestURI()).thenReturn("/test");

        dispatcherServlet.doGet(request, response);
        writer.flush();

        verify(response).setContentType("application/json");
        assertEquals("GET Response", stringWriter.toString());
    }

    @Test
    void testHandlePostRequest() throws Exception {
        when(request.getRequestURI()).thenReturn("/test");

        dispatcherServlet.doPost(request, response);
        writer.flush();

        verify(response).setContentType("application/json");
        assertEquals("POST Response", stringWriter.toString());
    }

    @Test
    void testHandleJsonResponse() throws Exception {
        when(request.getRequestURI()).thenReturn("/json");
        // Capture output here
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        // Create a fake ServletOutputStream
        ServletOutputStream servletOutputStream = new ServletOutputStream() {
            @Override
            public void write(int b) {
                byteStream.write(b); // redirect to byteStream
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener listener) {
            }
        };
        when(response.getOutputStream()).thenReturn(servletOutputStream);

        dispatcherServlet.doGet(request, response);

        verify(response).setContentType("application/json");
        assertTrue(byteStream.toString().contains("\"name\":\"Test\""));
        servletOutputStream.close();
        byteStream.close();
    }

    @Test
    void testHandleNonExistentPath() throws Exception {
        when(request.getRequestURI()).thenReturn("/nonexistent");

        dispatcherServlet.doGet(request, response);
        writer.flush();

        assertEquals("", stringWriter.toString());
    }

}