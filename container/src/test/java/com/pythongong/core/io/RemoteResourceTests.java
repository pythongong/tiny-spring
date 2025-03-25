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
package com.pythongong.core.io;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link RemoteResource}.
 * 
 * @author Cheng Gong
 */
public class RemoteResourceTests {

    private URL mockUrl;
    private HttpURLConnection mockConnection;

    @BeforeEach
    void setUp() throws Exception {
        mockUrl = mock(URL.class);
        mockConnection = mock(HttpURLConnection.class);
        when(mockUrl.openConnection()).thenReturn(mockConnection);
    }

    @Test
    @DisplayName("Constructor should throw exception when URL is null")
    void constructorShouldThrowExceptionWhenUrlIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new RemoteResource(null));
    }

    @Test
    @DisplayName("getInputStream should return input stream from URL connection")
    void getInputStreamShouldReturnInputStreamFromUrlConnection() throws IOException {
        // Arrange
        InputStream expectedStream = mock(InputStream.class);
        when(mockConnection.getInputStream()).thenReturn(expectedStream);
        RemoteResource resource = new RemoteResource(mockUrl);

        // Act
        InputStream result = resource.getInputStream();

        // Assert
        assertSame(expectedStream, result);
        verify(mockUrl).openConnection();
        verify(mockConnection).getInputStream();
    }

    @Test
    @DisplayName("getInputStream should disconnect HTTP connection when IOException occurs")
    void getInputStreamShouldDisconnectHttpConnectionWhenIOExceptionOccurs() throws IOException {
        // Arrange
        IOException expectedException = new IOException("Test exception");
        when(mockConnection.getInputStream()).thenThrow(expectedException);
        RemoteResource resource = new RemoteResource(mockUrl);

        // Act & Assert
        IOException exception = assertThrows(IOException.class, resource::getInputStream);
        assertSame(expectedException, exception);
        verify(mockUrl).openConnection();
        verify(mockConnection).getInputStream();
        verify(mockConnection).disconnect();
    }

    @Test
    @DisplayName("Integration test with real HTTP URL")
    void integrationTestWithRealHttpUrl() throws Exception {
        // Use a reliable endpoint for testing
        URL url = URI.create("https://httpbin.org/get").toURL();
        RemoteResource resource = new RemoteResource(url);

        try (InputStream inputStream = resource.getInputStream()) {
            assertNotNull(inputStream);
        }
    }
}