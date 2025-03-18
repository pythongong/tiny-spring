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
package com.pythongong.enums;

import java.lang.annotation.Annotation;

import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Value;

/**
 * Enumeration representing different types of field annotations supported in the framework.
 * Currently supports @Value and @AutoWired annotations for field injection.
 *
 * @author Cheng Gong
 */
public enum FiledAnnoEnum {
    
    /**
     * Represents the @Value annotation for property injection
     */
    VALUE(Value.class),

    /**
     * Represents the @AutoWired annotation for dependency injection
     */
    AUTO_WIRED(AutoWired.class);

    /**
     * The actual annotation class that this enum constant represents
     */
    private final Class<? extends Annotation> annotationClass;
    
    /**
     * Constructor for FiledAnnoEnum.
     *
     * @param annotationClass the annotation class this enum constant represents
     */
    FiledAnnoEnum(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    /**
     * Gets the enum constant corresponding to the given annotation class.
     *
     * @param annotationClass the annotation class to look up
     * @return the corresponding FiledAnnoEnum constant, or null if not found
     */
    public static FiledAnnoEnum fromAnnotation(Class<? extends Annotation> annotationClass) {
        for (FiledAnnoEnum type : values()) {
            if (type.annotationClass.equals(annotationClass)) {
                return type;
            }
        }
        return null;
    }
}