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
package com.pythongong.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a field for automatic dependency injection.
 * When applied to a field, the container will attempt to inject a bean
 * of the matching type or name into that field.
 *
 * <p>Example usage:
 * <pre>{@code
 * @Component
 * public class UserService {
 *     @AutoWired
 *     private UserRepository userRepository;
 *
 *     @AutoWired(name = "primaryDataSource")
 *     private DataSource dataSource;
 * }
 * }</pre>
 *
 * @author Cheng Gong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR})
@Documented
public @interface AutoWired {

    /**
     * The name of the bean to inject.
     * If not specified, the container will attempt to find a bean
     * matching the field's type.
     *
     * @return the name of the bean to inject, or empty string for type-based injection
     */
    String name() default "";
}