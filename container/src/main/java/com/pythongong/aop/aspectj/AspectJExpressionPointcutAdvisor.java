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

import com.pythongong.enums.AdviceEnum;
import com.pythongong.util.CheckUtils;

import lombok.Builder;

/**
 * Record class that holds the configuration for an AspectJ pointcut advisor.
 * This class combines the pointcut expression with its associated advice method
 * and metadata.
 *
 * <p>The advisor contains:
 * <ul>
 * <li>The name of the aspect bean that contains the advice</li>
 * <li>The type of advice (Before, After, Around, etc.)</li>
 * <li>The actual method that implements the advice</li>
 * <li>The pointcut expression that determines where the advice should be applied</li>
 * </ul>
 *
 * <p>This class is immutable and thread-safe, following the record pattern.
 * It uses the builder pattern for convenient instantiation.
 *
 * @author Cheng Gong
 * @see AspectJExpressionPointcut
 * @see AdviceEnum
 */
@Builder
public record AspectJExpressionPointcutAdvisor(
        /**
         * The name of the aspect bean containing the advice
         */
        String aspectName,

        /**
         * The type of advice (Before, After, Around, etc.)
         */
        AdviceEnum adviceEnum,

        /**
         * The method that implements the advice
         */
        Method method,

        /**
         * The pointcut expression that determines where the advice applies
         */
        AspectJExpressionPointcut pointcut) {

    /**
     * Compact constructor to validate the required fields.
     *
     * @throws IllegalArgumentException if method or pointcut is null
     */
    public AspectJExpressionPointcutAdvisor {
        CheckUtils.nullArgs(method, "AspectJExpressionPointcutAdvisor", "method");
        CheckUtils.nullArgs(pointcut, "AspectJExpressionPointcutAdvisor", "pointcut");
    }
}
