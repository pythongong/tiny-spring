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
package com.pythongong.beans.config;

import com.pythongong.exception.BeansException;
import com.pythongong.util.StringUtils;

/**
 * Represents a property value pair used in bean property configuration.
 * This record holds both the name of a property and its corresponding value.
 *
 * @param name  the name of the property
 * @param value the value to be set for the property, can be a direct value or a {@link BeanReference}
 * 
 * @author Cheng Gong
 */
public record PropertyValue(
    /**
     * The name of the property. Must not be null or empty.
     */
    String name,
    
    /**
     * The value to be set for the property. Can be a direct value or a {@link BeanReference}.
     */
    Object value
) {
    /**
     * Creates a new PropertyValue instance with validation.
     *
     * @param name  the name of the property
     * @param value the value of the property
     * @throws BeansException if name is null or empty
     */
    public PropertyValue {
        if (StringUtils.isEmpty(name)) {
            throw new BeansException("Property name cannot be null or empty");
        }
    }
}