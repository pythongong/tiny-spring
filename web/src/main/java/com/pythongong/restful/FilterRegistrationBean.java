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

import java.util.List;
import java.util.function.Supplier;

import com.pythongong.util.CheckUtils;

import jakarta.servlet.Filter;

/**
 * Registration holder for Servlet Filters.
 * 
 * <p>This record holds the configuration for a servlet filter including its
 * URL patterns and filter instance. Uses suppliers to allow lazy initialization
 * of the filter and its patterns.
 *
 * @author pythongong
 * @since 1.0
 */
public record FilterRegistrationBean(
        /** Supplier for the URL patterns this filter should be mapped to */
        Supplier<List<String>> getUrlPatterns,
        
        /** Supplier for the filter instance */
        Supplier<Filter> getFilter) {

    /**
     * Constructs a new filter registration with validation.
     *
     * @param getUrlPatterns supplier for URL patterns
     * @param getFilter supplier for filter instance
     * @throws IllegalArgumentException if any supplier is null
     */
    public FilterRegistrationBean {
        CheckUtils.nullArgs(getFilter, "FilterRegistrationBean", "getFilter");
        CheckUtils.nullArgs(getUrlPatterns, "FilterRegistrationBean", "getUrlPatterns");
    }

    /**
     * Gets the name of the filter for registration.
     *
     * @return the simple class name of the filter, or empty string if filter is null
     */
    public String getName() {
        if (getFilter == null || getFilter.get() == null) {
            return "";
        }
        return getFilter.get().getClass().getSimpleName();
    }
}
