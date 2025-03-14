

package com.pythongong.beans.config;

public record BeanDefinition(Class<?> beanClass, PropertyValueList propertyValueList
, String initMethodName, String destroyMethodName) {
    
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

