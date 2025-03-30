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
package com.pythongong.context.annotation;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.context.support.PropertyResolver;
import com.pythongong.aop.autoproxy.AspectJAutoProxyCreator;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.stereotype.ComponentScan;
import com.pythongong.stereotype.Configuration;
import com.pythongong.test.aop.valid.AopConfig;
import com.pythongong.test.ioc.normal.LifecycleTestBean;
import com.pythongong.test.ioc.normal.TestConfiguration;
import com.pythongong.test.ioc.normal.TestConfugrableBean;
import com.pythongong.util.ClassUtils;
import com.pythongong.enums.ScopeEnum;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfigurableClassParser Tests")
class ConfigurableClassParserTest {

    private PropertyResolver propertyResolver;
    private ConfigurableClassParser parser;

    @BeforeEach
    void setUp() {
        Properties props = new Properties();
        props.setProperty("test.property", "testValue");
        props.setProperty("test.name", "testValue\n");
        props.setProperty("test.version", "1.0.0\n");
        props.setProperty("app.description", "Test Application\n");
        propertyResolver = new PropertyResolver(props);
        parser = new ConfigurableClassParser(propertyResolver);
    }

    @Test
    @DisplayName("Should throw exception for null property resolver")
    void shouldThrowExceptionForNullPropertyResolver() {
        assertThrows(IllegalArgumentException.class,
                () -> new ConfigurableClassParser(null),
                "Should throw IllegalArgumentException for null PropertyResolver");
    }

    @Test
    @DisplayName("Should throw exception for null class")
    void shouldThrowExceptionForNullClass() {
        assertThrows(IllegalArgumentException.class,
                () -> parser.parse(null),
                "Should throw IllegalArgumentException for null class");
    }

    @Test
    @DisplayName("Should return empty set for non-annotated class")
    void shouldReturnEmptySetForNonAnnotatedClass() {
        Set<BeanDefinition> definitions = parser.parse(getClass());
        assertTrue(definitions.isEmpty(),
                "Should return empty set for class without ComponentScan");
    }

    @Test
    @DisplayName("Should parse ComponentScan with empty basePackages")
    void shouldParseComponentScanWithEmptyBasePackages() {
        Set<BeanDefinition> definitions = parser.parse(TestConfiguration.class);
        assertFalse(definitions.isEmpty(),
                "Should find components in the declaring class package");
    }

    @Test
    @DisplayName("Should parse ComponentScan with explicit basePackages")
    void shouldParseComponentScanWithExplicitBasePackages() {
        Set<BeanDefinition> definitions = parser.parse(TestConfiguration.class);
        assertFalse(definitions.isEmpty(),
                "Should find components in specified packages");
    }

    @Test
    @DisplayName("Should parse bean with lifecycle methods")
    void shouldParseBeanWithLifecycleMethods() {
        Set<BeanDefinition> definitions = parser.parse(TestConfiguration.class);

        BeanDefinition lifecycleBean = definitions.stream()
                .filter(def -> def.beanClass().equals(LifecycleTestBean.class))
                .findFirst()
                .orElse(null);

        assertNotNull(lifecycleBean, "Should find lifecycle component");
        assertNotNull(lifecycleBean.initMethodName(), "Should have init method");
        assertNotNull(lifecycleBean.destroyMethodName(), "Should have destroy method");
    }

    @Test
    @DisplayName("Should parse bean with custom scope")
    void shouldParseBeanWithCustomScope() {
        Set<BeanDefinition> definitions = parser.parse(TestConfiguration.class);

        BeanDefinition prototypeBean = definitions.stream()
                .filter(def -> def.scope().equals(ScopeEnum.PROTOTYPE))
                .findFirst()
                .orElse(null);

        assertNotNull(prototypeBean, "Should find prototype component");
        assertEquals(ScopeEnum.PROTOTYPE, prototypeBean.scope(),
                "Should have prototype scope");
    }

    @Test
    @DisplayName("Should parse configurable factory component")
    void shouldParseConfigurableFactoryComponent() {
        Set<BeanDefinition> definitions = parser.parse(TestConfiguration.class);

        BeanDefinition factoryBean = definitions.stream()
                .filter(def -> def.beanClass().equals(TestConfugrableBean.class))
                .findFirst()
                .orElse(null);

        assertNotNull(factoryBean);
        assertNotNull(factoryBean.factoryDefinition());

    }

    @Test
    @DisplayName("Should create AspectJAutoProxyCreator")
    void shouldCreateAspectJAutoProxyCreator() {
        Set<BeanDefinition> beanDefinitions = parser.parse(AopConfig.class);
        assertFalse(ClassUtils.isCollectionEmpty(beanDefinitions));
        List<BeanDefinition> aspectJAutoProxyCreators = beanDefinitions.stream()
                .filter(benDefinition -> {
                    return benDefinition.beanClass() == AspectJAutoProxyCreator.class;
                })
                .toList();
        assertTrue(aspectJAutoProxyCreators.size() == 1);
    }

    @Test
    @DisplayName("Should not create AspectJAutoProxyCreator")
    void shouldNotCreateAspectJAutoProxyCreator() {
        Set<BeanDefinition> beanDefinitions = parser.parse(TestConfiguration.class);
        assertFalse(ClassUtils.isCollectionEmpty(beanDefinitions));
        List<BeanDefinition> aspectJAutoProxyCreators = beanDefinitions.stream()
                .filter(benDefinition -> {
                    return benDefinition.beanClass() == AspectJAutoProxyCreator.class;
                })
                .toList();
        assertTrue(aspectJAutoProxyCreators.size() == 0);
    }

    // Test configurations
    @Configuration
    @ComponentScan(basePackages = {})
    static class EmptyBasePackagesConfig {
    }
}
