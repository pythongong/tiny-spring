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

package com.pythongong.utils;

import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

import com.pythongong.context.ApplicationContext;
import com.pythongong.context.impl.AnnotationConfigApplicationContext;
import com.pythongong.exception.WebException;
import com.pythongong.restful.DispatcherServlet;
import com.pythongong.restful.FilterRegistrationBean;
import com.pythongong.util.ClassUtils;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterRegistration;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletRegistration.Dynamic;

/**
 * Utility class for web-related operations.
 * 
 * <p>
 * Provides helper methods for servlet registration, URL pattern handling,
 * and filter configuration in the web container.
 *
 * @author pythongong
 * @since 1.0
 */
public class WebUtils {

    /** The name used for registering the dispatcher servlet */
    private static final String APP_SERVLET = "dispatcherServlet";

    /** The default URL pattern for servlet mapping */
    private static final String DEFAULT_MAPPING = "/";

    /**
     * Private constructor to prevent instantiation.
     */
    private WebUtils() {
    }

    /**
     * Registers the dispatcher servlet in the servlet context.
     * 
     * @param servletContext     the servlet context to register with
     * @param applicationContext the spring application context
     */
    public static void resgisterDispatcher(ServletContext servletContext, ApplicationContext applicationContext) {
        DispatcherServlet servlet = new DispatcherServlet(applicationContext);
        Dynamic dispatcherReg = servletContext.addServlet(APP_SERVLET, servlet);
        dispatcherReg.addMapping(DEFAULT_MAPPING);
        dispatcherReg.setLoadOnStartup(0);
    }

    /**
     * Generates a regex pattern for URL path matching.
     * 
     * @param url the URL pattern to convert
     * @return compiled Pattern for URL matching
     * @throws WebException if the URL pattern is invalid
     */
    public static Pattern generatePathPattern(String url) {
        // Finds patterns like {variableName} where variableName starts with a letter
        // and can contain letters/numbers
        // Converts them to named capture groups: (?<variableName>[^/]*)
        // [^/]* means "match any character except /" (zero or more times)
        String regPath = url.replaceAll("\\{([a-zA-Z][a-zA-Z0-9]*)\\}", "(?<$1>[^/]*)");
        if (regPath.indexOf('{') >= 0 || regPath.indexOf('}') >= 0) {
            throw new WebException("Invalid path: " + url);
        }
        // Adds ^ at start and $ at end to ensure the entire path matches
        return Pattern.compile("^" + regPath + "$");
    }

    /**
     * Registers filters in the servlet context.
     * 
     * @param servletContext     the servlet context to register with
     * @param applicationContext the spring application context
     * @throws WebException if filter configuration is invalid
     */
    public static void registerFilters(ServletContext servletContext, ApplicationContext applicationContext) {
        applicationContext = (AnnotationConfigApplicationContext) applicationContext;
        applicationContext.getBeansOfType(FilterRegistrationBean.class).forEach((name, bean) -> {
            List<String> urlPatterns = bean.getUrlPatterns.get();
            if (ClassUtils.isCollectionEmpty(urlPatterns)) {
                throw new WebException("");
            }
            Filter filter = bean.getFilter.get();
            if (filter == null) {
                throw new WebException("");
            }
            FilterRegistration.Dynamic filterReg = servletContext.addFilter(bean.getName(), filter);
            // DispatcherType.REQUEST means the filter will only be applied to
            // direct client requests
            // This filter will be executed earlier in the filter chain.
            filterReg.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true,
                    urlPatterns.toArray(String[]::new));

        });
    }
}
