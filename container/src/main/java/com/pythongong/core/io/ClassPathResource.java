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

import java.io.IOException;
import java.io.InputStream;

import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;
import com.pythongong.util.FileUtils;

/**
 * Resource implementation for class path resources.
 * Uses a ClassLoader to load resources from the class path.
 *
 * @author Cheng Gong
 */
public class ClassPathResource implements Resource {

    /**
     * The name of the resource file to load from the classpath
     */
    private String fileName;

    /**
     * The ClassLoader to use for loading the resource
     */
    private ClassLoader classLoader;

    /**
     * Creates a new ClassPathResource.
     *
     * @param fileName the name of the resource file to load
     */
    public ClassPathResource(String fileName) {
        CheckUtils.emptyString(fileName, "ClassPathResource recevies empty file name");
        this.fileName = fileName.substring(FileUtils.CLASSPATH_URL_PREFIX.length());
        this.classLoader = ClassUtils.getDefaultClassLoader();
    }

    /**
     * Returns an InputStream for reading from the classpath resource.
     *
     * @return an InputStream for the resource
     * @throws IOException if the resource cannot be opened
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return classLoader.getResourceAsStream(fileName);
    }
}