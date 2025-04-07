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
 * Annotation which indicates that a method parameter should be bound to a web request parameter.
 * 
 * <p>Supported for handler methods in controllers and binds request parameters
 * to method parameters. Method parameters using this annotation are required
 * by default.
 *
 * <p>Example usage:
 * <pre>
 * {@code @GetMapping("/search")}
 * public List<User> searchUsers(@RequestParam("query") String searchQuery) {
 *     // searchQuery will contain the value of the 'query' request parameter
 *     return userService.search(searchQuery);
 * }
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    /**
     * The name of the request parameter to bind to.
     *
     * @return the request parameter name
     */
    String value();
}
