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
 * Configures component scanning directives for use with @Configuration classes.
 * Provides support for specifying basePackages to scan for annotated components.
 *
 * <p>Example usage:
 * <pre>{@code
 * @Configuration
 * @ComponentScan(basePackages = {
 *     "com.example.repository",
 *     "com.example.service"
 * })
 * public class AppConfig {
 *     // ...
 * }
 * }</pre>
 *
 * @author Cheng Gong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface ComponentScan {

    /**
     * Base packages to scan for annotated components.
     * If not specified, scanning will occur from the package
     * of the class that declares this annotation.
     *
     * @return the base packages to scan
     */
    String[] basePackages() default {};
}
