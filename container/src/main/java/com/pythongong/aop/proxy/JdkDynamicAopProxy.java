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

import java.lang.reflect.Proxy;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.exception.AopConfigException;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;

/**
 * JDK Dynamic Proxy-based implementation of AOP proxy creation. This
 * implementation
 * uses Java's built-in dynamic proxy mechanism to create proxy instances for
 * interfaces.
 * 
 * <p>
 * This proxy creator generates proxy classes that implement the same interfaces
 * as the target object. It requires that the target object implements at least
 * one interface. All method calls on the proxy are intercepted by the
 * {@link AopInvocationHandler}.
 *
 * @author Cheng Gong
 * @see AopProxy
 * @see java.lang.reflect.Proxy
 */
public class JdkDynamicAopProxy implements AopProxy {

    /**
     * Configuration for the AOP proxy, including target object and interceptors.
     */
    private final AdvisedSupport advisedSupport;

    /**
     * Handler that implements the actual method interception logic.
     */
    private final AopInvocationHandler invocationHandler;

    /**
     * Creates a new JDK Dynamic Proxy-based AOP proxy creator.
     * Validates that the target object implements at least one interface.
     *
     * @param advisedSupport the AOP configuration containing target object and
     *                       interceptors
     * @throws IllegalArgumentException if advisedSupport is null
     * @throws AopConfigException       if the target object doesn't implement any
     *                                  interfaces
     */
    public JdkDynamicAopProxy(AdvisedSupport advisedSupport) {
        CheckUtils.nullArgs(advisedSupport, "JdkDynamicAopProxy recevies null advise");
        this.advisedSupport = advisedSupport;
        this.invocationHandler = new AopInvocationHandler(advisedSupport);
        Class<?>[] targetClasses = advisedSupport.getTargetInterfaces();
        if (ClassUtils.isArrayEmpty(targetClasses)) {
            throw new AopConfigException(
                    String.format("Target type {%s} is wrong", advisedSupport.getTargetClass().getName()));
        }
    }

    /**
     * Creates and returns a proxy instance that implements all interfaces
     * of the target object. The proxy uses the configured AopInvocationHandler
     * to intercept all method calls.
     *
     * @return the proxy instance
     * @see java.lang.reflect.Proxy#newProxyInstance
     */
    @Override
    public Object getProxy() {
        return Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(),
                advisedSupport.getTargetInterfaces(),
                invocationHandler);
    }
}
