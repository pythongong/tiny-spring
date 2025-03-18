

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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import com.pythongong.enums.ScopeEnum;
import lombok.Builder;

/**
 * Holds the definition of a bean including its class, scope, initialization methods,
 * and property values. This is used by the bean factory to create and configure bean instances.
 *
 * @author pythongong
 * @since 2025-03-18
 */
@Builder
public record BeanDefinition(
    /**
     * The name under which the bean is registered in the container.
     */
    String beanName,

    /**
     * The actual class of the bean to be instantiated.
     */
    Class<?> beanClass,

    /**
     * List of property values to be injected into the bean.
     * May be null, in which case an empty list will be created.
     */
    PropertyValueList propertyValueList,

    /**
     * Method to be called after bean properties are set.
     * May be null if no initialization method is specified.
     */
    Method initMethod,

    /**
     * Method to be called when the bean is being destroyed.
     * May be null if no destruction method is specified.
     */
    Method destroyMethod,

    /**
     * The scope of the bean (singleton or prototype).
     * Defaults to SINGLETON if not specified.
     */
    ScopeEnum scope,

    /**
     * The constructor to be used for instantiating the bean.
     * May be null, in which case the default constructor will be used.
     */
    Constructor<?> constructor
) {
    /**
     * Canonical constructor with default value handling.
     * Ensures propertyValueList is never null and scope has a default value.
     */
    public BeanDefinition {
        propertyValueList = propertyValueList == null ? new PropertyValueList() : propertyValueList;
        scope = scope != null ? scope : ScopeEnum.SINGLETON;
    }
}

