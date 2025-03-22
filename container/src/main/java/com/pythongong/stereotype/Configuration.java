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
 * Indicates that a class declares one or more @Bean methods and
 * may be processed by the container to generate bean definitions.
 * Configuration classes are typically the primary source of bean definitions.
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * {
 *     &#64;code
 *     &#64;Configuration
 *     &#64;ComponentScan("com.example")
 *     public class AppConfig {
 *         &#64;Bean
 *         public DataSource dataSource() {
 *             DataSource ds = new BasicDataSource();
 *             ds.setUrl("jdbc:mysql://localhost/db");
 *             return ds;
 *         }
 *
 *         @Bean
 *         public JdbcTemplate jdbcTemplate(DataSource dataSource) {
 *             return new JdbcTemplate(dataSource);
 *         }
 *     }
 * }
 * </pre>
 *
 * @author Cheng Gong
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configuration {

    /**
     * The value may indicate a suggestion for a logical component name.
     * If not specified, the container will generate a name based on
     * the class name.
     *
     * @return the suggested component name, if any
     */
    String value() default "";
}
