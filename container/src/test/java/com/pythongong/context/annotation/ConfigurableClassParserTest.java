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

import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.pythongong.context.support.PropertyResolver;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Component;
import com.pythongong.stereotype.ComponentScan;
import com.pythongong.stereotype.Configuration;
import com.pythongong.stereotype.PostConstruct;
import com.pythongong.stereotype.PreDestroy;
import com.pythongong.stereotype.Scope;
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
        propertyResolver = new PropertyResolver(props);
        parser = new ConfigurableClassParser(propertyResolver);
    }

    @Test
    @DisplayName("Should throw exception for null property resolver")
    void shouldThrowExceptionForNullPropertyResolver() {
        assertThrows(BeansException.class, 
            () -> new ConfigurableClassParser(null),
            "Should throw BeansException for null PropertyResolver");
    }

    @Test
    @DisplayName("Should throw exception for null class")
    void shouldThrowExceptionForNullClass() {
        assertThrows(BeansException.class, 
            () -> parser.parse(null),
            "Should throw BeansException for null class");
    }

    @Test
    @DisplayName("Should return empty set for non-annotated class")
    void shouldReturnEmptySetForNonAnnotatedClass() {
        Set<BeanDefinition> definitions = parser.parse(NonAnnotatedClass.class);
        assertTrue(definitions.isEmpty(), 
            "Should return empty set for class without ComponentScan");
    }

    @Test
    @DisplayName("Should parse ComponentScan with empty basePackages")
    void shouldParseComponentScanWithEmptyBasePackages() {
        Set<BeanDefinition> definitions = parser.parse(EmptyBasePackagesConfig.class);
        assertFalse(definitions.isEmpty(), 
            "Should find components in the declaring class package");
    }

    @Test
    @DisplayName("Should parse ComponentScan with explicit basePackages")
    void shouldParseComponentScanWithExplicitBasePackages() {
        Set<BeanDefinition> definitions = parser.parse(ExplicitBasePackagesConfig.class);
        assertFalse(definitions.isEmpty(), 
            "Should find components in specified packages");
    }

    @Test
    @DisplayName("Should parse bean with lifecycle methods")
    void shouldParseBeanWithLifecycleMethods() {
        Set<BeanDefinition> definitions = parser.parse(LifecycleConfig.class);
        
        BeanDefinition lifecycleBean = definitions.stream()
            .filter(def -> def.beanClass().equals(LifecycleComponent.class))
            .findFirst()
            .orElse(null);
            
        assertNotNull(lifecycleBean, "Should find lifecycle component");
        assertNotNull(lifecycleBean.initMethod(), "Should have init method");
        assertNotNull(lifecycleBean.destroyMethod(), "Should have destroy method");
    }

    @Test
    @DisplayName("Should parse bean with custom scope")
    void shouldParseBeanWithCustomScope() {
        Set<BeanDefinition> definitions = parser.parse(ScopeConfig.class);
        
        BeanDefinition prototypeBean = definitions.stream()
            .filter(def -> def.beanClass().equals(PrototypeComponent.class))
            .findFirst()
            .orElse(null);
            
        assertNotNull(prototypeBean, "Should find prototype component");
        assertEquals(ScopeEnum.PROTOTYPE, prototypeBean.scope(), 
            "Should have prototype scope");
    }

    // Test configurations
    @Configuration
    @ComponentScan(basePackages = {})
    static class EmptyBasePackagesConfig {}

    @Configuration
    @ComponentScan(basePackages = "com.pythongong.context.annotation")
    static class ExplicitBasePackagesConfig {}

    @Configuration
    @ComponentScan(basePackages = "com.pythongong.context.annotation")
    static class LifecycleConfig {}

    @Configuration
    @ComponentScan(basePackages = "com.pythongong.context.annotation")
    static class ScopeConfig {}

    static class NonAnnotatedClass {}

    // Test components
    @Component
    static class SimpleComponent {}

    @Component
    static class LifecycleComponent {
        @PostConstruct
        void init() {}
        
        @PreDestroy
        void destroy() {}
    }

    @Component
    @Scope(ScopeEnum.PROTOTYPE)
    static class PrototypeComponent {}
}