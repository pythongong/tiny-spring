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

package com.pythongong.boot;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.catalina.Server;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.LifecycleException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import com.pythongong.context.impl.PropertyResolver;
import com.pythongong.exception.WebException;
import com.pythongong.mock.restful.TestController;
import com.pythongong.util.ContextUtils;

class TinySpringApplicationTest {

    private TinySpringApplication application;
    private Server server;

    @BeforeEach
    void setUp() {
        application = new TinySpringApplication();
    }

    @AfterEach
    void tearDown() throws LifecycleException {
        application.close(server);
    }

    @Test
    void testStartServerWithDefaultPort() {
        server = application.startTomcat(TestController.class);
        assertNotNull(server);
        assertTrue(server.getState().isAvailable());
    }

    @Test
    void testStartServerWithCustomPort() throws IOException, URISyntaxException {
        PropertyResolver propertyResolver = new PropertyResolver();
        propertyResolver.addAll(Map.of("server.port", "8081"));
        ;
        try (MockedStatic<ContextUtils> mockedContext = mockStatic(ContextUtils.class)) {
            mockedContext.when(() -> ContextUtils.createPropertyResolver()).thenReturn(propertyResolver);
            server = application.startTomcat(TestController.class);
            Service service = server.findService("Tomcat");

            Connector[] connectors = service.findConnectors();
            for (Connector connector : connectors) {
                if ("HTTP/1.1".equals(connector.getProtocol())) {
                    assertTrue(connector.getPort() == 8081);
                }
            }
        }

    }

    @Test
    void testStartServerWithInvalidPort() {
        PropertyResolver propertyResolver = new PropertyResolver();
        propertyResolver.addAll(Map.of("server.port", "-1"));
        try (MockedStatic<ContextUtils> mockedContext = mockStatic(ContextUtils.class)) {
            mockedContext.when(() -> ContextUtils.createPropertyResolver()).thenReturn(propertyResolver);
            assertThrows(WebException.class, () -> application.start(getClass()));
        }

    }

    @Test
    void test() {
        TinySpringApplication.run(TestController.class, "ss");
        System.exit(0);
    }

}