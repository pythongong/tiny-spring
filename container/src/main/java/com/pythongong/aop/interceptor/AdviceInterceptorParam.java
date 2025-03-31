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

package com.pythongong.aop.interceptor;

import java.lang.reflect.Method;

import com.pythongong.util.CheckUtils;

/**
 * Parameter record for creating advice interceptors.
 * 
 * <p>Encapsulates the necessary parameters for creating an advice interceptor,
 * including the aspect instance and the advice method to be invoked.
 *
 * @author pythongong
 * @since 1.0
 * @param aspect the aspect instance containing the advice
 * @param method the advice method to be invoked
 */
public record AdviceInterceptorParam(
        /** The aspect instance containing the advice */
        Object aspect,
        /** The advice method to be invoked */
        Method method) {

    /**
     * Compact constructor for parameter validation.
     * @throws IllegalArgumentException if either aspect or method is null
     */
    public AdviceInterceptorParam {
        String methodName = "AdviceInterceptorParam";
        CheckUtils.nullArgs(aspect, methodName, "aspect");
        CheckUtils.nullArgs(method, methodName, "method");
    }
}