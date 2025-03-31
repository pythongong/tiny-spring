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

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import com.pythongong.util.CheckUtils;
import com.pythongong.util.FileUtils;

/**
 * Default implementation of the ResourceLoader interface.
 * Supports loading resources from the classpath, filesystem, and remote URLs.
 *
 * @author Cheng Gong
 */
public class DefaultResourceLoader implements ResourceLoader {

    /**
     * Returns a Resource based on the location type:
     * - Classpath resources (prefixed with "classpath:")
     * - URL resources (valid URLs)
     * - File system resources (everything else)
     *
     * @param location the location of the resource
     * @return the appropriate Resource implementation
     */
    @Override
    public Resource getResource(String location) {
        CheckUtils.emptyString(location, "DefaultResourceLoader.getResource receves empty location");
        if (location.startsWith(FileUtils.CLASSPATH_URL_PREFIX)) {
            return new ClassPathResource(location);
        }

        try {
            URL url = new URI(location).toURL();
            return new RemoteResource(url);
        } catch (MalformedURLException | URISyntaxException e) {
            return new FileSystemResource(location);
        }
    }
}