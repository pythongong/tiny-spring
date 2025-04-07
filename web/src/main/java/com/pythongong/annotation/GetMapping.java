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

package com.pythongong.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for mapping HTTP GET requests onto specific handler methods.
 * 
 * <p>Specifically, {@code @GetMapping} is a composed annotation that acts as
 * a shortcut for mapping HTTP GET requests. It supports a URL pattern through
 * its value attribute.
 *
 * <p>Example usage:
 * <pre>
 * {@code @GetMapping("/users/{id}")}
 * public User getUser(@PathVariable Long id) {
 *     // ... method implementation
 * }
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface GetMapping {

    /**
     * The URL path pattern to map requests to.
     * Defaults to empty string which maps to the base path.
     *
     * @return the URL path pattern
     */
    String value();
}
