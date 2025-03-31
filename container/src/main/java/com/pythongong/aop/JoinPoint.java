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

package com.pythongong.aop;

import com.pythongong.util.CheckUtils;

/**
 * Record representing a join point in the AOP context.
 * 
 * <p>Contains information about the method being intercepted,
 * including its name, parameter types, and arguments.
 *
 * @author pythongong
 * @since 1.0
 * @param methodName the name of the intercepted method
 * @param parameterTypes the parameter types of the intercepted method
 * @param args the arguments passed to the intercepted method
 */
public record JoinPoint(
        /** The name of the intercepted method */
        String methodName,
        /** The parameter types of the intercepted method */
        Class<?>[] parameterTypes,
        /** The arguments passed to the intercepted method */
        Object[] args) {

    /**
     * Compact constructor for parameter validation.
     * @throws IllegalArgumentException if methodName is null or empty
     */
    public JoinPoint {
        String errorMethd = "JoinPoint";
        CheckUtils.emptyString(methodName, errorMethd, "methodName");
    }
}
