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

/**
 * Factory interface for AOP proxies. Provides a common abstraction for different
 * proxy creation strategies such as JDK dynamic proxies and ByteBuddy-based proxies.
 * 
 * <p>Implementations of this interface are responsible for creating proxy instances
 * that provide method interception capabilities for AOP. The actual proxy creation
 * mechanism may vary depending on the implementation:
 * <ul>
 *   <li>JDK dynamic proxies for interface-based proxies</li>
 *   <li>ByteBuddy for class-based proxies</li>
 * </ul>
 *
 * @author Cheng Gong
 * @see JdkDynamicAopProxy
 * @see ByteBuddyAopProxy
 */
public interface AopProxy {

    /**
     * Creates and returns a new proxy instance. The proxy implements the
     * interception logic defined by the proxy creator's configuration.
     *
     * @return the proxy object
     * @throws com.pythongong.exception.AopException if proxy creation fails
     */
    Object getProxy();
}
