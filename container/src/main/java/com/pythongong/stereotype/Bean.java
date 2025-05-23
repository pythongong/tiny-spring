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
 * Indicates that a method produces a bean to be managed by the container.
 * The method must be defined within a @Configuration class.
 *
 * <p>
 * Example usage:
 * 
 * <pre>
 * {
 *     &#64;code
 *     &#64;Configuration
 *     public class AppConfig {
 *         @Bean
 *         public DataSource dataSource() {
 *             return new BasicDataSource();
 *         }
 *
 *         &#64;Bean("userService")
 *         public UserService userService() {
 *             return new UserServiceImpl();
 *         }
 *     }
 * }
 * </pre>
 *
 * @author Cheng Gong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Bean {

    /**
     * The name of the bean. If not specified, the container will
     * use the method name as the bean name.
     *
     * @return the name of the bean
     */
    String value() default "";

    /**
     * Initialize method name
     * 
     * @return the name of the initialization method
     */
    String init() default "";

    /**
     * Destroy method name
     * 
     * @return the name of the destroy method
     */
    String destroy() default "";
}