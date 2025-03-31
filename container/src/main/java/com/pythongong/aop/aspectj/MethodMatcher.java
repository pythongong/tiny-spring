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
package com.pythongong.aop.aspectj;

import java.lang.reflect.Method;

/**
 * Strategy interface for matching methods. This interface is used by pointcuts
 * to determine whether a method should be advised.
 *
 * <p>The {@code MethodMatcher} is designed to be used as part of the AOP
 * infrastructure, particularly in conjunction with AspectJ-style pointcuts.
 * It provides a simple way to determine if a method matches certain criteria.
 *
 * <p>Implementations can match methods based on various criteria such as:
 * <ul>
 * <li>Method name patterns</li>
 * <li>Parameter types</li>
 * <li>Return type</li>
 * <li>Annotations present on the method</li>
 * </ul>
 *
 * @author Cheng Gong
 * @see AspectJExpressionPointcut
 */
@FunctionalInterface
public interface MethodMatcher {

    /**
     * Determine whether this method matcher matches the given method.
     *
     * @param method the method to check
     * @return {@code true} if the method matches, {@code false} otherwise
     */
    boolean matches(Method method);
}
