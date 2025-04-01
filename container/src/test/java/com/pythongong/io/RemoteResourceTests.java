
package com.pythongong.io;

import org.junit.jupiter.api.Test;

import com.pythongong.io.RemoteResource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
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

}