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

import java.util.List;

import com.pythongong.aop.interceptor.MethodMatcherInterceptor;
import com.pythongong.stereotype.Nullable;
import com.pythongong.util.CheckUtils;

import lombok.Builder;

/**
 * Configuration holder for AOP proxy creation.
 * 
 * <p>
 * Holds the target object and its associated interceptors required
 * for creating AOP proxies.
 *
 * @author pythongong
 * @since 1.0
 * @param target                    the target object to be proxied
 * @param methodMatcherInterceptors list of method matchers and their
 *                                  interceptors
 */
@Builder
public record AdvisedSupport(
        /** The target object to be proxied */
        Object target,
        /** List of method matchers and their interceptors */
        List<MethodMatcherInterceptor> methodMatcherInterceptors) {

    /**
     * Compact constructor for parameter validation.
     * 
     * @throws IllegalArgumentException if target or methodMatcherInterceptors is
     *                                  null
     */
    public AdvisedSupport {
        CheckUtils.nullArgs(target, "AdvisedSupport recevies null targetSource");
        CheckUtils.nullArgs(methodMatcherInterceptors, "AdvisedSupport recevies null methodMatcherInterceptor");
    }

    /**
     * Gets the interfaces implemented by the target object.
     * 
     * @return array of interfaces, or null if none exist
     */
    @Nullable
    public Class<?>[] getTargetInterfaces() {
        return this.getTargetClass().getInterfaces();
    }

    /**
     * Gets the actual class of the target object.
     * 
     * @return the target object's class
     */
    public Class<?> getTargetClass() {
        return this.target.getClass();
    }
}