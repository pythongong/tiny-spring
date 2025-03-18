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
package com.pythongong.util;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import lombok.Builder;

/**
 * Record class that encapsulates parameters for classpath searching operations.
 * This class is used to configure how the classpath should be searched for resources,
 * including options for file system and JAR file searching.
 *
 * @param packagePath the base package path to search in
 * @param pathMapper consumer function that processes found paths
 * @param searchSudDirect whether to search in subdirectories
 * @param serachFile whether to search in the file system
 * @param serachJar whether to search in JAR files
 *
 * @author Cheng Gong
 */
@Builder
public record ClassPathSerchParam(
    String packagePath, 
    BiConsumer<Path, Path> pathMapper, 
    boolean searchSudDirect, 
    boolean serachFile, 
    boolean serachJar) {
}
