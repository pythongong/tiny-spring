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

/**
 * Strategy interface for loading resources. Implementations can support
 * loading resources from different locations such as classpath,
 * filesystem, or URLs.
 *
 * @author Cheng Gong
 */
public interface ResourceLoader {

    /**
     * Returns a Resource handle for the specified resource location.
     *
     * @param location the resource location
     * @return a Resource handle for the specified resource location
     */
    Resource getResource(String location);
} 