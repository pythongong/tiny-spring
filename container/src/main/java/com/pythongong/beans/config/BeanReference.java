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

/**
 * Represents a reference to another bean in the container.
 * This class is used to handle dependencies between beans during
 * the bean creation and wiring process.
 *
 * @author Cheng Gong
 */
public class BeanReference {
    
    /** Name of the referenced bean */
    private String beanName;

    /**
     * Returns the name of the referenced bean.
     *
     * @return the bean name
     */
    public String getBeanName() {
        return beanName;
    }

    /**
     * Creates a new bean reference with the specified bean name.
     *
     * @param beanName the name of the bean to reference
     */
    public BeanReference(String beanName) {
        this.beanName = beanName;
    }
}
