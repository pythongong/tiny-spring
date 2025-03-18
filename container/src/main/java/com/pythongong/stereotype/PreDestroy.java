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
 * Marks a method to be called right before a bean instance is destroyed by the container.
 * Methods annotated with @PreDestroy will be invoked during bean destruction,
 * allowing for cleanup of resources or any other necessary operations before
 * the bean is removed from the container.
 *
 * <p>The annotated method must:
 * <ul>
 *   <li>Have no parameters</li>
 *   <li>Return void</li>
 *   <li>Not throw checked exceptions</li>
 * </ul>
 * 
 * @author Cheng Gong
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface PreDestroy {
    
}
