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

import com.pythongong.stereotype.Nullable;
import com.pythongong.util.CheckUtils;

/**
 * Configuration holder for AOP proxies. This record encapsulates all the
 * necessary
 * components for creating and managing AOP proxies:
 * <ul>
 * <li>The target object to proxy</li>
 * <li>The interceptor that handles method invocations</li>
 * <li>The matcher that determines which methods to intercept</li>
 * </ul>
 * 
 * <p>
 * This class is immutable and thread-safe, implemented as a record to provide
 * a concise way to hold and access AOP configuration data.
 *
 * @param target            the target object to proxy
 * @param methodInterceptor the interceptor that handles method invocations
 * @param matcher           the matcher that determines which methods to
 *                          intercept
 * 
 * @author Cheng Gong
 * @see MethodInterceptor
 * @see MethodMatcher
 */
public record AdvisedSupport(Object target,
        MethodInterceptor methodInterceptor,
        MethodMatcher matcher) {

    /**
     * Constructs an AdvisedSupport instance with validation.
     * 
     * @throws IllegalArgumentException if any parameter is null
     */
    public AdvisedSupport {
        CheckUtils.nullArgs(target, "AdvisedSupport recevies null targetSource");
        CheckUtils.nullArgs(methodInterceptor, "AdvisedSupport recevies null methodInterceptor");
        CheckUtils.nullArgs(matcher, "AdvisedSupport recevies null matcher");
    }

    /**
     * Return the type of targets returned by this {@link TargetSource}.
     * <p>
     * Can return <code>null</code>, although certain usages of a
     * <code>TargetSource</code> might just work with a predetermined
     * target class.
     * 
     * @return the type of targets returned by this {@link TargetSource}
     */
    @Nullable
    public Class<?>[] getTargetInterfaces() {
        return this.target.getClass().getInterfaces();
    }

    /**
     * Returns the actual class of the target object.
     * This is useful for proxy creation mechanisms that need to
     * know the concrete class of the target.
     *
     * @return the Class object representing the target's type
     */
    public Class<?> getTargetClass() {
        return this.target.getClass();
    }
}