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

import com.pythongong.enums.ScopeEnum;

/**
 * Indicates the scope of a bean definition.
 * Can be applied to both component classes and @Bean methods.
 *
 * <p>Example usage:
 * <pre>{@code
 * @Component
 * @Scope(ScopeEnum.PROTOTYPE)
 * public class PrototypeBean {
 *     // ...
 * }
 *
 * @Configuration
 * public class Config {
 *     @Bean
 *     @Scope(ScopeEnum.SINGLETON)
 *     public SingletonBean singletonBean() {
 *         return new SingletonBean();
 *     }
 * }
 * }</pre>
 *
 * @author Cheng Gong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Scope {

    /**
     * Specifies the scope of the bean.
     * Defaults to singleton if not specified.
     *
     * @return the scope enum value
     */
    ScopeEnum value() default ScopeEnum.SINGLETON;
}