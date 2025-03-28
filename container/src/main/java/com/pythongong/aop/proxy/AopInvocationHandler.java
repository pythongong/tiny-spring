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

package com.pythongong.aop.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.exception.AopException;
import com.pythongong.util.CheckUtils;

/**
 * The invocation handler for AOP proxy. This class implements the core
 * interception
 * logic for method invocations in the AOP framework. It delegates method calls
 * either directly to the target object or through the configured method
 * interceptor
 * based on the pointcut matcher's decision.
 * 
 * <p>
 * This handler works in conjunction with JDK dynamic proxies to provide
 * method interception capabilities. When a method is called on the proxy:
 * <ul>
 * <li>If the method matches the pointcut, it will be intercepted and processed
 * by the configured {@code MethodInterceptor}</li>
 * <li>If the method doesn't match, it will be directly invoked on the target
 * object without interception</li>
 * </ul>
 * 
 * @author Cheng Gong
 * @see AdvisedSupport
 * @see java.lang.reflect.InvocationHandler
 */
public class AopInvocationHandler implements InvocationHandler {

    /**
     * The advised support holding the AOP configuration including target object,
     * method interceptor, and pointcut matcher.
     */
    private final AdvisedSupport advisedSupport;

    /**
     * Creates a new AOP invocation handler with the specified advised support.
     * 
     * @param advisedSupport the advised support containing the AOP configuration
     * @throws IllegalArgumentException if advisedSupport is null
     */
    public AopInvocationHandler(AdvisedSupport advisedSupport) {
        CheckUtils.nullArgs(advisedSupport, "AopInvocationHandler recevied null advise");
        this.advisedSupport = advisedSupport;
    }

    /**
     * Handles method invocations on the proxy instance. This method implements the
     * core interception logic:
     * <ol>
     * <li>Gets the target object from the advised support</li>
     * <li>Checks if the method should be intercepted using the matcher</li>
     * <li>If not matched, invokes the method directly on the target</li>
     * <li>If matched, delegates to the method interceptor for processing</li>
     * </ol>
     *
     * @param proxy  the proxy instance that the method was invoked on
     * @param method the Method instance corresponding to the interface method
     *               invoked on the proxy instance
     * @param args   an array of objects containing the values of the arguments
     *               passed in the method invocation
     * @return the result of the method invocation
     * @throws Throwable if the method invocation fails
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object target = advisedSupport.target();
        if (!advisedSupport.matcher().matches(method)) {
            return method.invoke(target, args);
        }
        return advisedSupport.methodInterceptor().invoke(() -> {
            try {
                return method.invoke(target, args);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AopException("Fail to invoke method ");
            }
        });
    }
}
