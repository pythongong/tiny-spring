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

import com.pythongong.stereotype.Bean;
import com.pythongong.stereotype.Configuration;

import jakarta.servlet.ServletContext;

/**
 * Configuration class for REST-related beans and settings.
 * 
 * <p>Provides configuration for servlet context and other web-related beans.
 * Uses static context holder pattern to make ServletContext available
 * throughout the application.
 *
 * @author pythongong
 * @since 1.0
 */
@Configuration
public class RestConfiguration {
    
    /** Holds the servlet context instance */
    private static ServletContext servletContext;

    /**
     * Sets the servlet context.
     *
     * @param context the ServletContext to be used
     */
    public static void setServletContext(ServletContext context) {
        servletContext = context;
    }

    /**
     * Creates a bean for the ServletContext.
     *
     * @return the configured ServletContext instance
     */
    @Bean
    ServletContext servletContext() {
        return servletContext;
    }
}
