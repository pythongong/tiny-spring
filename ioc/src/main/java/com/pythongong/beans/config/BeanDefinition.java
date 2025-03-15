

package com.pythongong.beans.config;

import java.lang.reflect.Method;

import lombok.EqualsAndHashCode;

public record BeanDefinition(String beanName, Class<?> beanClass, PropertyValueList propertyValueList
, Method initMethod, Method destroyMethod) {
    
    public BeanDefinition {
        if (propertyValueList == null) {
            propertyValueList = new PropertyValueList();
        }
    }

    public BeanDefinition(String beanName, Class<?> beanClass) {
        this(beanName, beanClass, new PropertyValueList(), null, null);
    }
}

