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
package com.pythongong.context;

import com.pythongong.beans.aware.Aware;
import com.pythongong.exception.BeansException;

/**
 * Interface to be implemented by any object that wishes to be notified of the
 * {@link ApplicationContext} it runs in.
 *
 * <p>Implementing this interface makes an object eligible for receiving the
 * ApplicationContext through the {@link #setApplicationContext} method.
 * The ApplicationContext will be injected after bean properties have been set
 * but before any custom initialization callbacks.
 *
 * @author Cheng Gong
 * @see ApplicationContext
 * @see Aware
 */
@FunctionalInterface
public interface ApplicationContextAware extends Aware {

    /**
     * Set the ApplicationContext that this object runs in.
     * <p>Normally this call will be used to initialize the object.
     * <p>This method is called after population of normal bean properties but
     * before an initialization callback such as InitializingBean's afterPropertiesSet.
     *
     * @param applicationContext the ApplicationContext object to be used by this object
     * @throws BeansException if the ApplicationContext setting fails
     */
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
