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

import com.pythongong.util.StringUtils;

/**
 * Enumeration representing the possible scopes for beans in the framework.
 * Supports singleton and prototype scopes, with singleton being the default.
 *
 * @author Cheng Gong
 */
public enum ScopeEnum {

    /**
     * Singleton scope - only one instance of the bean is created
     */
    SINGLETON("singleton"),

    /**
     * Prototype scope - a new instance is created each time the bean is requested
     */
    PROTOTYPE("prototype");

    /**
     * The string representation of the scope
     */
    private final String scope;

    /**
     * Constructor for ScopeEnum.
     *
     * @param scope the string representation of the scope
     */
    ScopeEnum(String scope) {
        this.scope = scope;
    }

    /**
     * Gets the enum constant from a scope string.
     * Returns SINGLETON if the scope string is empty.
     *
     * @param scope the scope string to convert
     * @return the corresponding ScopeEnum constant
     * @throws IllegalArgumentException if the scope string is invalid and not empty
     */
    public static ScopeEnum fromScope(String scope) {
        for (ScopeEnum scopeEnum : values()) {
            if (scopeEnum.scope.equals(scope)) {
                return scopeEnum;
            }
        }
        if (!StringUtils.isEmpty(scope)) {
            throw new IllegalArgumentException("Illegal scope: " + scope);
        }
        return SINGLETON;
    }
}