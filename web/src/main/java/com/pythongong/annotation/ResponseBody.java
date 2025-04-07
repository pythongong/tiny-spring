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
 * Annotation that indicates a method return value should be bound to the web response body.
 * 
 * <p>Supported at the method level, this annotation tells the framework that the
 * method return value should be serialized directly to the response body, typically
 * as JSON. It bypasses view resolution and renders the response content directly.
 *
 * <p>Example usage:
 * <pre>
 * {@code @GetMapping("/users/{id}")}
 * {@code @ResponseBody}
 * public User getUser(Long id) {
 *     // The returned User object will be automatically serialized to JSON
 *     return userService.findById(id);
 * }
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseBody {
}
