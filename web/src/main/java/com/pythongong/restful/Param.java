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

import com.pythongong.enums.ParamType;
import com.pythongong.util.CheckUtils;

/**
 * Represents a parameter descriptor for controller method parameters.
 * 
 * <p>
 * This record holds information about a parameter including its type,
 * class and name. Used for parameter resolution when handling HTTP requests.
 *
 * @author Cheng Gong
 * @since 1.0
 */
public record Param(
        /** The type of parameter (path variable, request param, etc) */
        ParamType paramType,

        /** The class type of the parameter */
        Class<?> classType,

        /** The name of the parameter */
        String name) {

    /**
     * Constructs a new parameter descriptor with validation.
     *
     * @param paramType the type of the parameter
     * @param classType the class type of the parameter
     * @param name      the name of the parameter
     * @throws IllegalArgumentException if any parameter is null or name is empty
     */
    public Param {
        CheckUtils.nullArgs(paramType, "Param", "paramType");
        CheckUtils.nullArgs(classType, "Param", "classType");
    }
}
