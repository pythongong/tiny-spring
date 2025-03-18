package com.pythongong.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * Annotation used for injecting values into fields in Spring-managed beans.
 * Supports property placeholder resolution and SpEL expression evaluation.
 *
 * <p>Values can be:
 * <ul>
 *     <li>Simple literals</li>
 *     <li>Property placeholders: ${property.name}</li>
 *     <li>System environment variables: ${ENV_VAR}</li>
 * </ul>
 *
 * <p>Example usage:
 * <pre>{@code
 * @Component
 * public class DatabaseConfig {
 *     @Value("${database.url}")
 *     private String databaseUrl;
 *
 *     @Value("${database.port:5432}")
 *     private int port;  // Uses default value 5432 if property not found
 * }
 * }</pre>
 *
 * @author Cheng Gong
 * @since 1.0
 *
 * @license Apache License 2.0
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
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    /**
     * The actual value expression: e.g. "#{systemProperties.myProp}", "${my.app.property}"
     */
    String value();
}
