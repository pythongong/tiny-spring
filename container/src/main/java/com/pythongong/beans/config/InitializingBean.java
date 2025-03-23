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
 * Interface to be implemented by beans that need to react once all their
 * properties have been set by the BeanFactory. Used to perform initialization
 * work after all properties have been set.
 *
 * @author Cheng Gong
 */
@FunctionalInterface
public interface InitializingBean {

    /**
     * Invoked by the containing BeanFactory after all properties have been set.
     *
     * @throws Exception in case of initialization errors
     */
    void afterPropertiesSet() throws Exception;
}