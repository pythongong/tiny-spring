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

import com.pythongong.stereotype.Component;

/**
 * Indicates that an annotated class is a "Service" (e.g. a business service facade).
 * 
 * <p>This annotation serves as a specialization of {@code @Component},
 * allowing for implementation classes to be autodetected through classpath scanning.
 * It is typically used to indicate that a class is holding the business logic
 * and exists as a stateless service.
 *
 * <p>Example usage:
 * <pre>
 * {@code @Service}
 * public class UserService {
 *     public User createUser(String username) {
 *         // Business logic here
 *     }
 * }
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 * @see Component
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Service {

    /**
     * The value may indicate a suggestion for a logical component name.
     *
     * @return the suggested component name, if any
     */
    String value() default "";
}
