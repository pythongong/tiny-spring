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
 * Annotation for injecting values into fields, methods, parameters, or annotations.
 * Supports property placeholder resolution and value expressions.
 *
 * <p>Supported value formats include:
 * <ul>
 *     <li>Property placeholders: ${property.name}</li>
 *     <li>System environment variables: ${ENV_VAR}</li>
 *     <li>Default values: ${property.name:defaultValue}</li>
 *     <li>Simple literals: value</li>
 * </ul>
 *
 * <p>Example usages:
 * <pre>{@code
 * @Component
 * public class DatabaseConfig {
 *     // Basic property injection
 *     @Value("${database.url}")
 *     private String databaseUrl;
 *
 *     // With default value
 *     @Value("${database.port:5432}")
 *     private int port;
 *
 *     // Environment variable
 *     @Value("${DATABASE_PASSWORD}")
 *     private String password;
 *
 *     // Literal value
 *     @Value("default-pool-size")
 *     private String poolName;
 *
 *     // Method parameter injection
 *     public void setMaxConnections(@Value("${db.max.connections:100}") int maxConnections) {
 *         // ...
 *     }
 * }
 * }</pre>
 *
 * <p>The resolution of values follows this order:
 * <ol>
 *     <li>System properties</li>
 *     <li>Environment variables</li>
 *     <li>Application properties</li>
 *     <li>Default value (if provided)</li>
 * </ol>
 *
 * @author Cheng Gong
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    /**
     * The actual value expression to inject.
     * This can be a property placeholder, environment variable reference,
     * or literal value.
     *
     * @return the value expression to resolve
     */
    String value();
}
