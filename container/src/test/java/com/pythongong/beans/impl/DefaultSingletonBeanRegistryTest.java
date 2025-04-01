/*
 * Copyright (c) 2023 [Your Name or Organization]
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

package com.pythongong.beans.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.pythongong.beans.config.DisposableBean;

/**
 * Test class for DefaultSingletonBeanRegistry
 * Verifies the singleton bean registration, retrieval and destruction
 * functionality
 */
public class DefaultSingletonBeanRegistryTest {

    private DefaultSingletonBeanRegistry registry;

    @BeforeEach
    void setUp() {
        registry = new DefaultSingletonBeanRegistry();
    }

    @Test
    void testGetAndRegisterSingleton() {
        Object bean = new Object();
        String beanName = "testBean";

        registry.registerSingleton(beanName, bean);
        assertSame(bean, registry.getSingleton(beanName));
    }

    @Test
    void testRegisterSingletonWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> registry.registerSingleton("", new Object()));
    }

    @Test
    void testRegisterSingletonWithNullBean() {
        assertThrows(IllegalArgumentException.class, () -> registry.registerSingleton("test", null));
    }

    @Test
    void testRegisterAndDestroyDisposableBean() throws Exception {
        DisposableBean disposableBean = mock(DisposableBean.class);
        String beanName = "disposableBean";

        registry.registerDisposableBean(beanName, disposableBean);
        registry.destroySingletons();

        verify(disposableBean, times(1)).destroy();
    }

    @Test
    void testRegisterDisposableBeanWithEmptyName() {
        assertThrows(IllegalArgumentException.class,
                () -> registry.registerDisposableBean("", mock(DisposableBean.class)));
    }

    @Test
    void testRegisterDisposableBeanWithNullBean() {
        assertThrows(IllegalArgumentException.class, () -> registry.registerDisposableBean("test", null));
    }

}