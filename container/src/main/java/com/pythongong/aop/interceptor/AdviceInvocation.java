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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.pythongong.aop.JoinPoint;
import com.pythongong.exception.AopConfigException;
import com.pythongong.util.CheckUtils;

import lombok.Builder;

/**
 * Represents an invocation of an advised method, which handles the execution chain
 * of method interceptors and the target method.
 *
 * <p>This record maintains the state of the current invocation including the target
 * object, method, join point, and the chain of interceptors to be applied.
 *
 * @author pythongong
 * @since 1.0
 * @param target the target object on which the method is being invoked
 * @param method the method being invoked
 * @param joinPoint the join point representing the method invocation
 * @param methodMatcherInterceptors the list of interceptors to be applied
 * @param interceptedNum the current position in the interceptor chain
 */
@Builder
public record AdviceInvocation(
        /** The target object on which the method is being invoked */
        Object target,
        /** The method being invoked */
        Method method,
        /** The join point representing the method invocation */
        JoinPoint joinPoint,
        /** The list of interceptors to be applied */
        List<MethodMatcherInterceptor> methodMatcherInterceptors,
        /** The current position in the interceptor chain */
        AtomicInteger interceptedNum) {

    /**
     * Compact constructor for parameter validation.
     * @throws IllegalArgumentException if any required parameter is null or empty
     */
    public AdviceInvocation {
        String name = "AdviceInvocation";
        CheckUtils.nullArgs(target, name, "target");
        CheckUtils.nullArgs(method, name, "method");
        CheckUtils.nullArgs(joinPoint, name, "jointPoint");
        CheckUtils.emptyCollection(methodMatcherInterceptors, name, "methodMatcherInterceptors");

        interceptedNum = new AtomicInteger(0);
    }

    /**
     * Proceeds with the invocation chain.
     * <p>Either invokes the next interceptor in the chain or proceeds to the target method
     * if all interceptors have been executed.
     *
     * @return the result of the invocation
     */
    public Object proceed() {
        if (interceptedNum.get() == (methodMatcherInterceptors.size())) {
            return invokeRealMethod();
        }
        int interceptorIndex = interceptedNum.getAndIncrement();
        return methodMatcherInterceptors.get(interceptorIndex).methodInterceptor().invoke(this);
    }

    /**
     * Invokes the actual target method.
     * @return the result of the target method invocation
     * @throws AopConfigException if method invocation fails
     */
    private Object invokeRealMethod() {
        try {
            method.setAccessible(true);
            return method.invoke(target, joinPoint.args());
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new AopConfigException("method invoke in failure");
        }
    }
}
