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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import com.pythongong.util.CheckUtils;

/**
 * Resource implementation for files in the file system.
 * Supports reading resources from the local file system.
 *
 * @author Cheng Gong
 */
public class FileSystemResource implements Resource {

    /**
     * The file representing this resource
     */
    private File file;

    /**
     * Creates a new FileSystemResource from a File object.
     *
     * @param file the File object representing the resource
     */
    public FileSystemResource(File file) {
        CheckUtils.nullArgs(file, "FileSystemResource recevies null file");
        this.file = file;
    }

    /**
     * Creates a new FileSystemResource from a file path.
     *
     * @param path the path to the file
     */
    public FileSystemResource(String path) {
        this.file = new File(path);
    }

    /**
     * Returns an InputStream for reading from the file.
     *
     * @return an InputStream for the file
     * @throws IOException if the file cannot be opened
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(file);
    }
}
