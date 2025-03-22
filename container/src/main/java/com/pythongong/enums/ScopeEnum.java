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
    SINGLETON(),

    /**
     * Prototype scope - a new instance is created each time the bean is requested
     */
    PROTOTYPE();

}