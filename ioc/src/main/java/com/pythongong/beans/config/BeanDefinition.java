

package com.pythongong.beans.config;

public class BeanDefinition {
    
    private Class<?> beanClass;

    private PropertyValueList propertyValueList;

    public BeanDefinition(Class<?> beanClass, PropertyValueList propertyValueList) {
        this.beanClass = beanClass;
        this.propertyValueList = propertyValueList == null ? new PropertyValueList() : propertyValueList;
    }

    public BeanDefinition(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public PropertyValueList getPropertyValueList() {
        return propertyValueList;
    }

}

