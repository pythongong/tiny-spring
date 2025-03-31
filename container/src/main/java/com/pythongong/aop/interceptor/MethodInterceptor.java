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

import com.pythongong.exception.AopConfigException;

/**
 * Interface for method interception in AOP.
 * 
 * <p>Provides the core interception mechanism for AOP advice, allowing custom
 * behavior to be inserted before, after, or around method invocations.
 *
 * @author pythongong
 * @since 1.0
 */
@FunctionalInterface
public interface MethodInterceptor {

    /**
     * Intercepts a method invocation.
     * 
     * @param invocation the context of the method invocation
     * @return the result of the method invocation
     * @throws AopConfigException if an error occurs during interception
     */
    Object invoke(AdviceInvocation invocation) throws AopConfigException;
}
