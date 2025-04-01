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

package com.pythongong.util;

import java.lang.reflect.Method;
import com.pythongong.aop.JoinPoint;
import com.pythongong.aop.ProceedingJoinPoint;
import lombok.Builder;

/**
 * Parameter holder for advice method invocation.
 * 
 * <p>Encapsulates all necessary parameters required for invoking
 * an advice method, including the method itself, join points, and return values.
 *
 * @author pythongong
 * @since 1.0
 * @param advicMethod the advice method to be invoked
 * @param joinPoint the join point representing the method invocation
 * @param proceedingJoinPoint the proceeding join point for around advice
 * @param retVal the return value from method execution
 * @param aspect the aspect instance containing the advice
 */
@Builder
public record AdviceMethodParam(
        /** The advice method to be invoked */
        Method advicMethod,
        /** The join point representing the method invocation */
        JoinPoint joinPoint,
        /** The proceeding join point for around advice */
        ProceedingJoinPoint proceedingJoinPoint,
        /** The return value from method execution */
        Object retVal,
        /** The aspect instance containing the advice */
        Object aspect) {

    /**
     * Compact constructor for parameter validation.
     * @throws IllegalArgumentException if advicMethod or aspect is null
     */
    public AdviceMethodParam {
        String methodName = "AdviceMethodParam";
        CheckUtils.nullArgs(advicMethod, methodName, "advicMethod");
        CheckUtils.nullArgs(aspect, methodName, "aspect");
    }
}
