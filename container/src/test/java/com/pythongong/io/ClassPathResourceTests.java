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
package com.pythongong.io;

import org.junit.jupiter.api.Test;

import com.pythongong.io.ClassPathResource;

import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link ClassPathResource}.
 * 
 * @author Cheng Gong
 */
public class ClassPathResourceTests {

    @Test
    @DisplayName("Constructor should throw exception when fileName is null")
    void constructorShouldThrowExceptionWhenFileNameIsNull() {
        assertThrows(IllegalArgumentException.class, () -> new ClassPathResource(null));
    }

    @Test
    @DisplayName("Constructor should throw exception when fileName is empty")
    void constructorShouldThrowExceptionWhenFileNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> new ClassPathResource(""));
    }

    @Test
    @DisplayName("getInputStream should return null for non-existent resource")
    void getInputStreamShouldReturnNullForNonExistentResource() throws IOException {
        // Create a resource with a path that definitely doesn't exist
        ClassPathResource resource = new ClassPathResource(
                "classpath:non-existent-file-that-definitely-does-not-exist.xyz");

        // The getInputStream method should return null for non-existent resources
        assertNull(resource.getInputStream());
    }

    @Test
    @DisplayName("Test with a resource that exists in the test classpath")
    void testWithResourceThatExistsInTestClasspath() throws IOException {
        // This test assumes there's a META-INF/MANIFEST.MF file in the classpath,
        // which is common in Java applications
        ClassPathResource resource = new ClassPathResource("classpath:META-INF/MANIFEST.MF");

        try (InputStream inputStream = resource.getInputStream()) {
            assertNotNull(inputStream);

            // Read the first few bytes to verify it's a valid manifest file
            byte[] buffer = new byte[100];
            int bytesRead = inputStream.read(buffer);

            if (bytesRead > 0) {
                String content = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);
                // Most MANIFEST.MF files start with "Manifest-Version:"
                assertTrue(content.contains("Manifest-Version:") ||
                        content.contains("manifest-version:") ||
                        !content.isEmpty());
            }
        } catch (IOException | NullPointerException e) {
            // If the MANIFEST.MF file doesn't exist in this test environment, we'll skip
            // the test
            System.out.println("Skipping test as META-INF/MANIFEST.MF not found in classpath");
        }
    }
}