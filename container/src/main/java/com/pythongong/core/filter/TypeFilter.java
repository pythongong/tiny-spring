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
package com.pythongong.core.filter;

/**
 * Strategy interface for filtering classes during classpath scanning.
 * Implementations can define custom matching logic to determine
 * whether a given class should be included in the scanning results.
 *
 * @author Cheng Gong
 */
public interface TypeFilter {
    
    /**
     * Determine whether the given class matches the filter criteria.
     *
     * @param target the class to check
     * @return true if the class matches, false otherwise
     */
    boolean match(Class<?> target);
}