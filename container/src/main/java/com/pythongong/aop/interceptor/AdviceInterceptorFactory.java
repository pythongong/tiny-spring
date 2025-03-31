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

import com.pythongong.aop.ProceedingJoinPoint;
import com.pythongong.enums.AdviceEnum;
import com.pythongong.stereotype.Nullable;
import com.pythongong.util.AdviceMethodParam;
import com.pythongong.util.AopUtils;

/**
 * Factory for creating AOP advice interceptors based on different advice types.
 * 
 * <p>Provides factory methods to create various types of method interceptors
 * for handling different AspectJ advice types (Before, Around, After, AfterReturning).
 *
 * @author pythongong
 * @since 1.0
 */
public class AdviceInterceptorFactory {

    /**
     * Creates an appropriate advice interceptor based on the advice type.
     * @param param the parameters needed for creating the advice
     * @param adviceEnum the type of advice to create
     * @return the created method interceptor, or null if the advice type is not supported
     */
    @Nullable
    public static MethodInterceptor createAdvice(AdviceInterceptorParam param, AdviceEnum adviceEnum) {
        switch (adviceEnum) {
            case Before:
                return beforeAdvice(param);

            case Around:
                return aroundAdvice(param);

            case After:
                return afterAdvice(param);

            case AfterReturning:
                return afterReturningAdvice(param);

            default:
                return null;

        }
    }

    /**
     * Creates a before advice interceptor that executes advice before the target method.
     * @param param the parameters needed for creating the advice
     * @return the before advice interceptor
     */
    public static MethodInterceptor beforeAdvice(AdviceInterceptorParam param) {
        return (invocation) -> {
            AopUtils.invokeAdvice(AdviceMethodParam.builder()
                    .advicMethod(param.method())
                    .aspect(param.aspect())
                    .joinPoint(invocation.joinPoint())
                    .build());
            return invocation.proceed();
        };
    }

    /**
     * Creates an around advice interceptor that wraps the target method execution.
     * @param param the parameters needed for creating the advice
     * @return the around advice interceptor
     */
    public static MethodInterceptor aroundAdvice(AdviceInterceptorParam param) {
        return (invocation) -> {
            ProceedingJoinPoint proceedingJoinPoint = new ProceedingJoinPoint(invocation);
            return AopUtils.invokeAdvice(AdviceMethodParam.builder()
                    .advicMethod(param.method())
                    .aspect(param.aspect())
                    .proceedingJoinPoint(proceedingJoinPoint)
                    .build());
        };
    }

    /**
     * Creates an after advice interceptor that executes advice after the target method
     * (in a finally block).
     * @param param the parameters needed for creating the advice
     * @return the after advice interceptor
     */
    public static MethodInterceptor afterAdvice(AdviceInterceptorParam param) {
        return (invocation) -> {
            Object result;
            try {
                result = invocation.proceed();
            } finally {
                AopUtils.invokeAdvice(AdviceMethodParam.builder()
                        .advicMethod(param.method())
                        .aspect(param.aspect())
                        .joinPoint(invocation.joinPoint())
                        .build());
            }
            return result;

        };
    }

    /**
     * Creates an after-returning advice interceptor that executes advice after successful
     * completion of the target method.
     * @param param the parameters needed for creating the advice
     * @return the after-returning advice interceptor
     */
    public static MethodInterceptor afterReturningAdvice(AdviceInterceptorParam param) {
        return (invocation) -> {
            Object retVal = invocation.proceed();
            Object newRetVal = AopUtils.invokeAdvice(AdviceMethodParam.builder()
                    .advicMethod(param.method())
                    .aspect(param.aspect())
                    .joinPoint(invocation.joinPoint())
                    .retVal(retVal)
                    .build());
            return newRetVal != null ? newRetVal : retVal;
        };
    }
}
