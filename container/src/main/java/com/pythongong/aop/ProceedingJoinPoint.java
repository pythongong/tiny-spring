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

import com.pythongong.aop.interceptor.AdviceInvocation;
import com.pythongong.util.CheckUtils;

/**
 * Represents a proceeding join point in the AOP context, specifically for around advice.
 * 
 * <p>Provides access to the underlying invocation and allows control over the
 * method execution chain, including the ability to proceed with the invocation.
 *
 * @author pythongong
 * @since 1.0
 */
public class ProceedingJoinPoint {

    /** The underlying invocation being processed */
    private final AdviceInvocation invocation;

    /**
     * Constructs a new ProceedingJoinPoint with the given invocation.
     * @param invocation the advice invocation to be processed
     * @throws IllegalArgumentException if invocation is null
     */
    public ProceedingJoinPoint(AdviceInvocation invocation) {
        CheckUtils.nullArgs(invocation, "ProceedingJoinPoint","invocation");
        this.invocation = invocation;
    }

    /**
     * Proceeds with the next interceptor in the chain.
     * @return the result of proceeding with the interceptor chain
     */
    public Object proceed() {
        return invocation.proceed();
    }

    /**
     * Gets the join point representing the current method invocation.
     * @return the current join point
     */
    public JoinPoint getJoinPoint() {
        return invocation.joinPoint();
    }
}
