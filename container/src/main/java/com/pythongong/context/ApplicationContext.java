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


import com.pythongong.beans.factory.ListableBeanFactory;
import com.pythongong.context.event.ApplicationEventPublisher;
import com.pythongong.exception.BeansException;

/**
 * Central interface to provide configuration for an application.
 * This is read-only while the application is running, but may be
 * reloaded if the implementation supports this.
 *
 * <p>The interface provides:
 * <ul>
 *   <li>Bean factory methods for accessing application components
 *   <li>Ability to load file resources
 *   <li>Ability to publish events to registered listeners
 *   <li>Support for hierarchical contexts
 * </ul>
 *
 * @author Cheng Gong
 * @see ListableBeanFactory
 * @see ApplicationEventPublisher
 */
public interface ApplicationContext extends ListableBeanFactory, ApplicationEventPublisher {

    /**
     * Load or refresh the persistent representation of the configuration.
     * The process includes:
     * <ul>
     *   <li>Loading bean definitions
     *   <li>Post-processing bean factory
     *   <li>Registering bean post-processors
     *   <li>Initializing message source
     *   <li>Initializing event multicaster
     *   <li>Instantiating singletons
     * </ul>
     * 
     * @throws BeansException if the refresh fails
     */
    void refresh() throws BeansException;

    /**
     * Register a shutdown hook with the JVM runtime.
     * Calls {@link #close()} when the JVM shuts down.
     */
    void registerShutdownHook();

    /**
     * Close this application context, releasing all resources and
     * destroying all cached singleton beans.
     */
    void close();
}
