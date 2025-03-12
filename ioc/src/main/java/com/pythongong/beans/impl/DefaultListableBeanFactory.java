package com.pythongong.beans.impl;

import java.util.HashMap;
import java.util.Map;

import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.exception.IocException;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry{

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>();
    
    @Override
    protected BeanDefinition getBeanDefinition(String beanName) throws IocException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new IocException("No bean named '" + beanName + "' is defined");
        }
        return beanDefinition;
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
    }
    
    
}
