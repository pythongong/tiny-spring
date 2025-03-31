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

import com.pythongong.aop.aspectj.MethodMatcher;
import com.pythongong.util.CheckUtils;

/**
 * Record that combines a method interceptor with its corresponding method matcher.
 * 
 * <p>Pairs an interceptor that provides the advice behavior with a matcher that
 * determines which methods the interceptor should be applied to.
 *
 * @author pythongong
 * @since 1.0
 * @param methodInterceptor the interceptor providing the advice behavior
 * @param methodMatcher the matcher determining method applicability
 */
public record MethodMatcherInterceptor(
        /** The interceptor providing the advice behavior */
        MethodInterceptor methodInterceptor,
        /** The matcher determining method applicability */
        MethodMatcher methodMatcher) {

    /**
     * Compact constructor for parameter validation.
     * @throws IllegalArgumentException if either methodInterceptor or methodMatcher is null
     */
    public MethodMatcherInterceptor {
        String methodName = "MethodMatcherInterceptor";
        CheckUtils.nullArgs(methodInterceptor, methodName, "methodInterceptor");
        CheckUtils.nullArgs(methodMatcher, methodName, "methodMatcher");
    }
}
