/*
 * Copyright 2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pythongong.restful;

/**
 * Record representing the result of request processing.
 * 
 * <p>Holds information about whether a request was processed
 * and its return value if any.
 *
 * @author Cheng Gong
 * @since 1.0
 * @param isProcessed whether the request was processed
 * @param retVal the return value from processing, if any
 */
public record Result(
        /** Whether the request was processed */
        boolean isProcessed,
        /** The return value from processing */
        Object retVal) {
}
