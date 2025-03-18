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
package com.pythongong.beans.support;

import java.lang.reflect.Method;
import com.pythongong.beans.config.DisposableBean;
import com.pythongong.util.CheckUtils;

/**
 * Adapter that implements the DisposableBean interface for any bean that needs to be
 * cleaned up on container shutdown.
 * <p>This adapter handles both DisposableBean implementations and custom destroy methods,
 * making it possible to invoke bean destruction through a unified interface.
 *
 * @author Cheng Gong
 * @see DisposableBean
 */
public class DisposableBeanAdapter implements DisposableBean {

    /** The bean instance that needs to be destroyed */
    private final Object bean;

    /** The custom destroy method to be invoked */
    private final Method destroyMethod;

    /**
     * Creates a new DisposableBeanAdapter.
     *
     * @param bean the bean instance that needs destruction
     * @param destroyMethod the method to be called for cleanup
     * @throws IllegalArgumentException if bean or destroyMethod is null
     */
    public DisposableBeanAdapter(Object bean, Method destroyMethod) {
        CheckUtils.nullArgs(bean, "DisposableBeanAdapter recevies null bean");
        CheckUtils.nullArgs(destroyMethod, "DisposableBeanAdapter recevies null destroyMethod");

        this.bean = bean;
        this.destroyMethod = destroyMethod;
    }

    /**
     * Invokes bean destruction by:
     * 1. Calling destroy() if the bean implements DisposableBean
     * 2. Invoking the custom destroy method
     *
     * @throws Exception if an error occurs during destruction
     */
    @Override
    public void destroy() throws Exception {
        if (bean instanceof DisposableBean) {
            ((DisposableBean) bean).destroy();
        }
        destroyMethod.invoke(bean);
    }
}