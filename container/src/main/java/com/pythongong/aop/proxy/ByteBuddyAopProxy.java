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

import java.lang.reflect.InvocationTargetException;
import com.pythongong.aop.AdvisedSupport;
import com.pythongong.exception.AopConfigException;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * ByteBuddy-based implementation of AOP proxy creation. This implementation
 * uses
 * ByteBuddy to generate proxy classes at runtime, providing an alternative to
 * JDK dynamic proxies with better performance and support for class proxies.
 * 
 * <p>
 * This proxy creator generates subclasses of the target class and intercepts
 * all public methods using the configured {@link AopInvocationHandler}. The
 * generated proxies maintain the class hierarchy and interface implementations
 * of the target class.
 *
 * @author Cheng Gong
 * @see AopProxy
 * @see ByteBuddy
 */
public class ByteBuddyAopProxy implements AopProxy {

    /**
     * Shared ByteBuddy instance for creating proxy classes.
     * ByteBuddy is thread-safe and can be reused across multiple proxy creations.
     */
    private final static ByteBuddy byteBuddy = new ByteBuddy();

    /**
     * Configuration for the AOP proxy, including target object and interceptors.
     */
    private final AdvisedSupport advisedSupport;

    /**
     * Handler that implements the actual method interception logic.
     */
    private final AopInvocationHandler invocationHandler;

    /**
     * Creates a new ByteBuddy-based AOP proxy creator.
     *
     * @param advisedSupport the AOP configuration containing target object and
     *                       interceptors
     * @throws IllegalArgumentException if advisedSupport is null
     */
    public ByteBuddyAopProxy(AdvisedSupport advisedSupport) {
        CheckUtils.nullArgs(advisedSupport, "ByteBuddyAopProxy recevies null advise");
        this.advisedSupport = advisedSupport;
        invocationHandler = new AopInvocationHandler(advisedSupport);
    }

    /**
     * Creates and returns a proxy instance for the configured target class.
     * The proxy is created using ByteBuddy with the following characteristics:
     * <ul>
     * <li>Extends the target class</li>
     * <li>Uses the default constructor</li>
     * <li>Intercepts all public methods</li>
     * <li>Delegates method calls to the AopInvocationHandler</li>
     * </ul>
     *
     * @return the proxy instance
     * @throws AopConfigException if proxy creation or instantiation fails
     */
    @Override
    public Object getProxy() {
        Class<?> beanClass = advisedSupport.getTargetClass();
        Class<?> proxyClass = byteBuddy
                .subclass(beanClass, ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
                .method(ElementMatchers.isPublic())
                .intercept(InvocationHandlerAdapter.of(invocationHandler))
                .make().load(ClassUtils.getDefaultClassLoader())
                .getLoaded();

        try {
            return proxyClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new AopConfigException("Fail to invoke method ");
        }
    }
}
