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
package com.pythongong.beans.aware;

/**
 * Interface to be implemented by beans that wish to be aware of their
 * ClassLoader. Usually used when a bean needs to perform dynamic class loading.
 *
 * @author Cheng Gong
 */
public interface BeanClassLoaderAware extends Aware {
    
    /**
     * Supply the ClassLoader to a bean instance.
     * Invoked after population of normal bean properties but before initialization.
     *
     * @param classLoader the ClassLoader to be used
     */
    void setBeanClassLoader(ClassLoader classLoader);
}