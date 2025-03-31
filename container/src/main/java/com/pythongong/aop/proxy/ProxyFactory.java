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

package com.pythongong.aop.proxy;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.util.CheckUtils;

/**
 * Factory for creating AOP proxies based on the target class type.
 * 
 * <p>Creates appropriate proxy instances using either JDK dynamic proxies
 * for interfaces or ByteBuddy for concrete classes.
 *
 * @author pythongong
 * @since 1.0
 */
public class ProxyFactory {

    /**
     * Creates an AOP proxy for the given advised support.
     * 
     * <p>Uses JDK dynamic proxy for interfaces and ByteBuddy for concrete classes.
     *
     * @param advisedSupport the advised support containing target and interceptors
     * @return the created proxy object
     * @throws IllegalArgumentException if advisedSupport is null
     */
    public static Object createProxy(AdvisedSupport advisedSupport) {
        CheckUtils.nullArgs(advisedSupport, "ProxyFactory recevies null advise");
        Object target = advisedSupport.target();
        Class<?> targetClass = target.getClass();
        if (targetClass.isInterface()) {
            return new JdkDynamicAopProxy(advisedSupport).getProxy();
        }
        return new ByteBuddyAopProxy(advisedSupport).getProxy();
    }
}
