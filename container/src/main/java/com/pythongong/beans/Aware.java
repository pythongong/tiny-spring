package com.pythongong.beans;
/**
 * Marker interface to be implemented by beans that wish to be notified of their
 * running environment by the container. Serves as a parent interface for all
 * awareness-based interfaces in the framework.
 *
 * <p>A bean implementing this interface will receive a callback during the bean
 * initialization process, allowing it to be notified of the environment it's
 * running in or to access specific container facilities.
 *
 * <p>Common Aware interfaces include:
 * <ul>
 *     <li>{@link BeanAware} - Provides access to the BeanFactory</li>
 *     <li>{@link BeanNameAware} - Notifies of the bean's name</li>
 * </ul>
 *
 * @author Cheng Gong
 * @since 1.0
 *
 * @license Apache License 2.0
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
public interface Aware {
    // Marker interface - no methods to implement
}
