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
package com.pythongong.context.annotation;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.pythongong.core.filter.AnnotationTypeFilter;
import com.pythongong.core.filter.TypeFilter;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Component;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassPathSerchParam;
import com.pythongong.util.PathUtils;
import com.pythongong.util.StringUtils;

/**
 * Configurable scanner that detects candidate components on the classpath.
 * <p>By default, identifies classes annotated with the {@link Component} annotation.
 * Can be configured with custom type filters to identify additional component types.
 * Scans both classes in directories and within JAR files.
 * 
 * @author Cheng Gong
 * @see Component
 * @see TypeFilter
 * @see AnnotationTypeFilter
 */
public class ConfigurableClassScanner {
    
    /** List of filters that determine matching classes */
    private final List<TypeFilter> includeFilters;

    /**
     * Creates a new scanner with default filters.
     * By default, includes the {@link Component} annotation filter.
     */
    public ConfigurableClassScanner() {
        this(null);
    }

    /**
     * Creates a new scanner with the specified filters.
     * Always adds the {@link Component} annotation filter to the provided list.
     *
     * @param includeFilters the filters to use, or null for defaults only
     */
    public ConfigurableClassScanner(List<TypeFilter> includeFilters) {
        this.includeFilters = includeFilters == null ? new ArrayList<>() : includeFilters;
        this.includeFilters.add(new AnnotationTypeFilter(Component.class));
    }

    /**
     * Adds an include filter to the inclusion list.
     *
     * @param includeFilter the filter to add
     */
    public void addIncludeFilter(TypeFilter includeFilter) {
        this.includeFilters.add(includeFilter);
    }

    /**
     * Scan one or more base packages for candidate components.
     *
     * @param basePackages the packages to scan
     * @return a set of candidate component classes
     * @throws IllegalArgumentException if basePackages is empty
     * @throws BeansException if a candidate class cannot be loaded
     */
    public Set<Class<?>> scan(String... basePackages) {
        CheckUtils.emptyArray(basePackages, "ConfigurableClassScanner.scan recevies empty package names");

        Set<Class<?>> beanClasses = new HashSet<>();
        for (String basePackage : basePackages) {
            beanClasses.addAll(scanCandidateComponents(basePackage));
        }
        return beanClasses;
    }

    /**
     * Scans a package for candidate components.
     *
     * @param basePackage the package to scan
     * @return set of candidate classes from the package
     * @throws BeansException if class loading fails
     */
    private Set<Class<?>> scanCandidateComponents(String basePackage) {
        Set<String> classNames = new HashSet<>();
        String packagePath = PathUtils.convertPackageToPath(basePackage);

        PathUtils.findClassPathFileNames(ClassPathSerchParam.builder()
            .packagePath(packagePath)
            .serachJar(true)
            .serachFile(true)
            .searchSudDirect(true)
            .pathMapper((basePath, filePath) -> {
                String filePathStr = filePath.toString();
                if (!filePathStr.endsWith(PathUtils.CLASS_FILE_SUFFIX)) {
                    return;
                }
                String basePathStr = basePath.toString();
                // For Jar file system, it's 0
                int startIndex = basePathStr.length() - packagePath.length();
                int endIndex = filePathStr.length() - PathUtils.CLASS_FILE_SUFFIX.length();
                String className = filePathStr.substring(startIndex, endIndex);
                className = className.replace(PathUtils.PATH_SEPARATOR, PathUtils.PACKAGE_SEPARATOR)
                    .replace(PathUtils.SYSTEM_PATH_SEPARATOR, PathUtils.PACKAGE_SEPARATOR);
                if (!StringUtils.isEmpty(className)) {
                    classNames.add(className);
                }
            })
            .build());

        Set<Class<?>> beanClasses = new HashSet<>();
        classNames.forEach(className -> {
            try {
                Class<?> clazz = Class.forName(className);
                if (isCandidateComponent(clazz)) {
                    beanClasses.add(clazz);
                }
            } catch (ClassNotFoundException e) {
                throw new BeansException(String.format("Can not find class: {%s} in packge: {%s} ", className, basePackage), e);
            }
        });
        return beanClasses;
    }

    /**
     * Checks if a class is a candidate component by applying the configured filters.
     * Class must not be private or abstract to be considered a candidate.
     *
     * @param clazz the class to check
     * @return true if the class is a candidate component
     * @throws BeansException if the class has invalid modifiers
     */
    private boolean isCandidateComponent(Class<?> clazz) {
        for (TypeFilter typeFilter : includeFilters) {
            if (!typeFilter.match(clazz)) {
                continue;
            }
            int modifiers = clazz.getModifiers();
            if (Modifier.isPrivate(modifiers) || Modifier.isAbstract(modifiers)) {
                throw new BeansException(String.format("Class: {%s}'s' modifer has invalid midifier", clazz.getName()));
            }
            return true;
        }
        return false;
    }
}