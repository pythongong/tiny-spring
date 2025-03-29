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

import com.pythongong.exception.AopConfigException;

/**
 * Functional interface for implementing method interception in AOP.
 * Interceptors can perform custom logic before and after method execution,
 * or even completely replace the original method's behavior.
 * 
 * <p>
 * This interface is designed to work with Java's Supplier interface,
 * allowing for a clean and functional approach to method interception.
 * The proceed parameter represents the original method execution,
 * which can be invoked or skipped as needed.
 * 
 * <p>
 * Example usage:
 * 
 * <pre>
 * MethodInterceptor interceptor = proceed -> {
 *     // Before method execution
 *     Object result = proceed.get();
 *     // After method execution
 *     return result;
 * };
 * </pre>
 *
 * @author Cheng Gong
 * @see java.util.function.Supplier
 * @see com.pythongong.exception.AopConfigException
 */
@FunctionalInterface
public interface MethodInterceptor {

    /**
     * Intercepts a method invocation and handles its execution.
     *
     * @param proceed a supplier that executes the original method
     * @return the result of the method invocation
     * @throws AopConfigException if an error occurs during interception
     */
    Object invoke(MethodInvocation invocation) throws AopConfigException;
}
