
package com.pythongong.beans.impl;

import java.util.HashMap;
import java.util.Map;

import com.pythongong.beans.SingletonBeanReigistry;

public class DefaultSingletonBeanRegistry implements SingletonBeanReigistry{

    private final Map<String, Object> singletonObjects = new HashMap<>();

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    
}