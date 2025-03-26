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
package com.pythongong.aop;

import java.lang.reflect.Method;

/**
 * Strategy interface for matching methods in AOP pointcuts.
 * Implementations can define custom logic to determine whether
 * a given method should be intercepted by AOP advice.
 * 
 * <p>This interface is designed to be simple and flexible,
 * allowing for various matching strategies such as:
 * <ul>
 *   <li>Method name patterns</li>
 *   <li>Annotation presence</li>
 *   <li>Method signature patterns</li>
 *   <li>Custom logic based on method attributes</li>
 * </ul>
 *
 * @author Cheng Gong
 * @see java.lang.reflect.Method
 */
public interface MethodMatcher {

    /**
     * Tests whether this matcher matches the given method.
     * 
     * @param method the method to check
     * @return true if the method matches, false otherwise
     */
    boolean matches(Method method);
}
