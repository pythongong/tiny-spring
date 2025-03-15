

package com.pythongong.beans.config;

import java.lang.reflect.Method;

import com.pythongong.enums.ScopeEnum;

import lombok.Builder;

@Builder
public record BeanDefinition(String beanName, Class<?> beanClass, PropertyValueList propertyValueList
, Method initMethod, Method destroyMethod, ScopeEnum scope) {
    
    public BeanDefinition {
        propertyValueList = propertyValueList == null ? new PropertyValueList() : propertyValueList;
        scope = scope != null ? scope : ScopeEnum.SINGLETON;
    }
}

