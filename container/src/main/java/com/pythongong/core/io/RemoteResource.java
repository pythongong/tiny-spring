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
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Resource implementation for URLs.
 * Supports reading resources from remote locations via HTTP or other URL protocols.
 *
 * @author Cheng Gong
 */
public class RemoteResource implements Resource {

    /**
     * The URL of the remote resource
     */
    private final URL url;

    /**
     * Creates a new RemoteResource for the given URL.
     *
     * @param url the URL of the remote resource
     */
    public RemoteResource(URL url) {
        this.url = url;
    }

    /**
     * Opens a connection to the remote resource and returns an input stream.
     * For HTTP connections, ensures proper cleanup by disconnecting if an error occurs.
     *
     * @return an InputStream for reading from the remote resource
     * @throws IOException if an I/O error occurs
     */
    @Override
    public InputStream getInputStream() throws IOException {
        URLConnection connection = url.openConnection();
        try {
            return connection.getInputStream();
        } catch (IOException e) {
            if (connection instanceof HttpURLConnection) {
                ((HttpURLConnection)connection).disconnect();
            }
            throw e;
        }
    }
}