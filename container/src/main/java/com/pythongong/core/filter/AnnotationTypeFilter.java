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
package com.pythongong.core.filter;

import java.lang.annotation.Annotation;

import com.pythongong.util.ClassUtils;

/**
 * A TypeFilter implementation that checks for the presence of a specific annotation.
 * This filter can detect annotations that are either directly present on a class
 * or meta-present through other annotations.
 *
 * @author Cheng Gong
 */
public class AnnotationTypeFilter implements TypeFilter {

    /**
     * The annotation type to look for
     */
    private final Class<? extends Annotation> annotationType;

    /**
     * Creates a new AnnotationTypeFilter for the given annotation type.
     *
     * @param annotationType the type of annotation to look for
     */
    public AnnotationTypeFilter(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    /**
     * Checks whether the target class has the specified annotation.
     * Uses ClassUtils.findAnnotation to support meta-annotations.
     *
     * @param target the class to check for the annotation
     * @return true if the annotation is found, false otherwise
     */
    @Override
    public boolean match(Class<?> target) {
        Object object = ClassUtils.findAnnotation(target, annotationType);
        return object != null;
    }
}