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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.io.TempDir;

import com.pythongong.exception.BeansException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link FileSystemResource}.
 * 
 * @author Cheng Gong
 */
public class FileSystemResourceTests {

    @Test
    @DisplayName("Constructor should throw exception when File is null")
    void constructorShouldThrowExceptionWhenFileIsNull() {
        assertThrows(BeansException.class, () -> new FileSystemResource((File) null));
    }

    @Test
    @DisplayName("Constructor with String path should create a valid resource")
    void constructorWithStringPathShouldCreateValidResource(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("test.txt");
        String testContent = "Test content for FileSystemResource";
        Files.write(testFile, testContent.getBytes(StandardCharsets.UTF_8));

        // Act
        FileSystemResource resource = new FileSystemResource(testFile.toString());

        // Assert
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String content = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            assertEquals(testContent, content);
        }
    }

    @Test
    @DisplayName("Constructor with File object should create a valid resource")
    void constructorWithFileObjectShouldCreateValidResource(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("test.txt");
        String testContent = "Test content for FileSystemResource";
        Files.write(testFile, testContent.getBytes(StandardCharsets.UTF_8));

        // Act
        FileSystemResource resource = new FileSystemResource(testFile.toFile());

        // Assert
        try (InputStream inputStream = resource.getInputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead = inputStream.read(buffer);
            String content = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
            assertEquals(testContent, content);
        }
    }

    @Test
    @DisplayName("getInputStream should throw IOException when file does not exist")
    void getInputStreamShouldThrowIOExceptionWhenFileDoesNotExist() {
        // Arrange
        File nonExistentFile = new File("non-existent-file.txt");
        FileSystemResource resource = new FileSystemResource(nonExistentFile);

        // Act & Assert
        assertThrows(IOException.class, resource::getInputStream);
    }

    @Test
    @DisplayName("getInputStream should return a new stream each time it's called")
    void getInputStreamShouldReturnNewStreamEachTime(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("test.txt");
        String testContent = "Test content for FileSystemResource";
        Files.write(testFile, testContent.getBytes(StandardCharsets.UTF_8));
        FileSystemResource resource = new FileSystemResource(testFile.toFile());

        // Act
        InputStream stream1 = resource.getInputStream();
        InputStream stream2 = resource.getInputStream();

        // Assert
        assertNotSame(stream1, stream2);

        // Cleanup
        stream1.close();
        stream2.close();
    }

    @Test
    @DisplayName("Multiple resources can read from the same file")
    void multipleResourcesCanReadFromSameFile(@TempDir Path tempDir) throws IOException {
        // Arrange
        Path testFile = tempDir.resolve("test.txt");
        String testContent = "Test content for FileSystemResource";
        Files.write(testFile, testContent.getBytes(StandardCharsets.UTF_8));

        FileSystemResource resource1 = new FileSystemResource(testFile.toFile());
        FileSystemResource resource2 = new FileSystemResource(testFile.toString());

        // Act & Assert
        try (InputStream stream1 = resource1.getInputStream();
                InputStream stream2 = resource2.getInputStream()) {

            byte[] buffer1 = new byte[1024];
            byte[] buffer2 = new byte[1024];

            int bytesRead1 = stream1.read(buffer1);
            int bytesRead2 = stream2.read(buffer2);

            String content1 = new String(buffer1, 0, bytesRead1, StandardCharsets.UTF_8);
            String content2 = new String(buffer2, 0, bytesRead2, StandardCharsets.UTF_8);

            assertEquals(testContent, content1);
            assertEquals(testContent, content2);
        }
    }
}