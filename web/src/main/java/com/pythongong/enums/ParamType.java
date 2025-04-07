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

package com.pythongong.enums;

import java.lang.annotation.Annotation;

import com.pythongong.annotation.PathVariable;
import com.pythongong.annotation.RequestBody;
import com.pythongong.annotation.RequestParam;

/**
 * Enumeration of parameter types supported in web request handlers.
 * 
 * <p>This enum maps different parameter annotations to their corresponding
 * parameter types. It supports path variables, request parameters, and
 * request body parameters.
 *
 * @author Cheng Gong
 * @since 1.0
 */
public enum ParamType {
    /** Path variable parameter type */
    PATH_VARIABLE(PathVariable.class), 
    
    /** Request parameter type */
    REQUEST_PARAM(RequestParam.class), 
    
    /** Request body parameter type */
    REQUEST_BODY(RequestBody.class);

    /** The annotation class associated with this parameter type */
    private final Class<? extends Annotation> anonClass;

    /**
     * Constructs a new parameter type with its associated annotation class.
     *
     * @param anonClass the annotation class
     */
    private ParamType(Class<? extends Annotation> anonClass) {
        this.anonClass = anonClass;
    }

    /**
     * Gets the parameter type from an annotation class.
     *
     * @param annotationType the annotation class to look up
     * @return the corresponding parameter type, or null if not found
     */
    public static ParamType fromTypes(Class<? extends Annotation> annotationType) {
        for (ParamType paramType : values()) {
            if (paramType.anonClass == annotationType) {
                return paramType;
            } 
        }
        return null;
    }
}
