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
 * A convenience annotation that is itself annotated with {@code @Component}.
 * 
 * <p>Types that carry this annotation are treated as controllers where every
 * method returns a response body. This is exactly the same as annotating all
 * your methods with {@code @ResponseBody}.
 *
 * <p>Example usage:
 * <pre>
 * {@code @RestController}
 * public class UserController {
 *     {@code @GetMapping("/users")}
 *     public List<User> listUsers() {
 *         // This will be automatically converted to JSON
 *         return userService.findAll();
 *     }
 * }
 * </pre>
 *
 * @author Cheng Gong
 * @since 1.0
 * @see Component
 * @see ResponseBody
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface RestController {

    /**
     * The value may indicate a suggestion for a logical component name.
     *
     * @return the suggested component name, if any
     */
    String value() default "";
}
