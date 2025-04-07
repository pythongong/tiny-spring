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

package com.pythongong.restful;

import java.util.Set;

import com.pythongong.context.ApplicationContext;
import com.pythongong.context.impl.AnnotationConfigApplicationContext;
import com.pythongong.context.impl.PropertyResolver;
import com.pythongong.util.CheckUtils;
import com.pythongong.utils.WebUtils;

import jakarta.servlet.ServletContainerInitializer;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

/**
 * Initializer for the Spring web application context.
 * 
 * <p>Implements ServletContainerInitializer to bootstrap the Spring application
 * context during servlet container startup. Handles character encoding configuration
 * and registers the dispatcher servlet and filters.
 *
 * @author pythongong
 * @since 1.0
 */
public class ContextLoaderInitialize implements ServletContainerInitializer {

    /** The configuration class for the application context */
    private final Class<?> configClass;

    /** The property resolver for configuration values */
    private final PropertyResolver propertyResolver;

    /**
     * Constructs a new context loader with the specified configuration.
     * 
     * @param configClass the configuration class for the application context
     * @param propertyResolver the property resolver for configuration values
     */
    public ContextLoaderInitialize(Class<?> configClass, PropertyResolver propertyResolver) {
        CheckUtils.nullArgs(configClass, "ContextLoaderInitialize", "configClass");
        CheckUtils.nullArgs(propertyResolver, "ContextLoaderInitialize", "propertyResolver");
        this.configClass = configClass;
        this.propertyResolver = propertyResolver;
    }

    /**
     * Called by the servlet container to initialize the web application.
     * 
     * @param arg0 set of classes from classpath scanning (unused)
     * @param servletContext the servlet context to be initialized
     * @throws ServletException if initialization fails
     */
    @Override
    public void onStartup(Set<Class<?>> arg0, ServletContext servletContext) throws ServletException {
        String encoding = propertyResolver.getProperty("${tiny-spring.web.character-encoding:UTF-8}");
        servletContext.setRequestCharacterEncoding(encoding);
        servletContext.setResponseCharacterEncoding(encoding);
        RestConfiguration.setServletContext(servletContext);
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(configClass);
        WebUtils.resgisterDispatcher(servletContext, applicationContext);

        WebUtils.registerFilters(servletContext, applicationContext);
    }
}
