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
package com.pythongong.util;

import java.lang.annotation.Annotation;
import java.util.Collection;

/**
 * Utility class for common class-related operations.
 * This class provides functionality for class loading, annotation finding,
 * and array operations commonly used throughout the tiny-spring framework.
 * 
 * @author Cheng Gong
 */
public class ClassUtils {

    /** Private constructor to prevent instantiation of utility class */
    private ClassUtils() {
    }

    public static int BIG_INITIAL_SIZE = 256;

    /**
     * Default bean name for the application event multicaster
     */
    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    /**
     * Gets the default ClassLoader to use.
     * First tries to get the thread context ClassLoader, if that fails,
     * uses the ClassLoader that loaded this class.
     *
     * @return the default ClassLoader to use
     */
    public static ClassLoader getDefaultClassLoader() {
        ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
        return contextClassLoader == null ? ClassUtils.class.getClassLoader() : contextClassLoader;
    }

    /**
     * Recursively searches for an annotation on a given class.
     * This method will search through meta-annotations if the target annotation
     * is not directly present on the class.
     *
     * @param <A>       the type of annotation to find
     * @param target    the class to search on
     * @param annoClass the annotation class to look for
     * @return the found annotation or null if not found
     */
    public static <A extends Annotation> A findAnnotation(Class<?> target, Class<A> annoClass) {
        A targetAnno = target.getAnnotation(annoClass);
        if (targetAnno != null) {
            return targetAnno;
        }
        for (Annotation anno : target.getAnnotations()) {
            Class<? extends Annotation> annoType = anno.annotationType();
            if (annoType.getPackageName().equals("java.lang.annotation")) {
                continue;
            }
            targetAnno = findAnnotation(annoType, annoClass);
            if (targetAnno != null) {
                break;
            }
        }
        return targetAnno;
    }

    /**
     * Checks if an array is null or empty.
     *
     * @param array the array to check
     * @return true if the array is null or empty, false otherwise
     */
    public static boolean isArrayEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    public static boolean isCollectionEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
