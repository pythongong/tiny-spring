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
package com.pythongong.beans;

import com.pythongong.exception.BeansException;

/**
 * Interface to be implemented by bean factories that are capable of
 * autowiring, provided that they want to expose this functionality for
 * existing bean instances.
 *
 * @author Cheng Gong
 */
public interface AutowireCapableBeanFactory extends BeanFactory {

    /**
     * Apply BeanPostProcessors to the given existing bean instance,
     * invoking their postProcessBeforeInitialization methods.
     *
     * @param existingBean the existing bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one
     * @throws BeansException in case of errors
     */
    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException;

    /**
     * Apply BeanPostProcessors to the given existing bean instance,
     * invoking their postProcessAfterInitialization methods.
     *
     * @param existingBean the existing bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one
     * @throws BeansException in case of errors
     */
    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName) throws BeansException;
}