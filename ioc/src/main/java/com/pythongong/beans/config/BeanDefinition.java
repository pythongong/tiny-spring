

package com.pythongong.beans.config;

import java.lang.reflect.Method;

public record BeanDefinition(Class<?> beanClass, PropertyValueList propertyValueList
, Method initMethod, Method destroyMethod) {
    
    public BeanDefinition {
        if (propertyValueList == null) {
            propertyValueList = new PropertyValueList();
        }
    }

    public BeanDefinition(Class<?> beanClass) {
        this(beanClass, new PropertyValueList(), null, null);
    }

    public BeanDefinition(Class<?> beanClass, PropertyValueList propertyValueList) {
        this(beanClass, propertyValueList, null, null);
    }

}

