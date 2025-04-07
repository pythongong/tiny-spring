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
 * Annotation which indicates that a method parameter should be bound to a URI template
 * variable.
 * 
 * <p>Supported for {@code @GetMapping} and {@code @PostMapping} annotated handler methods.
 * The value of the path variable will be automatically converted to the target parameter type.
 *
 * <p>Example usage:
 * <pre>
 * {@code @GetMapping("/users/{id}")}
 * public User getUser(@PathVariable("id") Long userId) {
 *     // ... method implementation
 * }
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PathVariable {

    /**
     * The name of the path variable to bind to.
     *
     * @return the path variable name
     */
    String value();
}
