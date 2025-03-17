package com.pythongong.beans.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.FactoryBean;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.exception.BeansException;
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassUtils;

public class FactoryBeanRegistrySupport {

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCachedObjectForFactoryBean(String beanName) {
        CheckUtils.emptyString(beanName, "FactoryBeanRegistrySupport.getCachedObjectForFactoryBean recevies empty bean name");
        Object cachedObject = factoryBeanObjectCache.get(beanName);
        return cachedObject == ClassUtils.NULL_OBJECT ? null : cachedObject;
    }

    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, BeanDefinition beanDefinition) {
        CheckUtils.nullArgs(beanDefinition, "FactoryBeanRegistrySupport.getObjectFromFactoryBean recevies null bean definition");
        CheckUtils.nullArgs(factory, "FactoryBeanRegistrySupport.getObjectFromFactoryBean recevies null factory bean");

        Object curObject = null;
        String beanName = beanDefinition.beanName();
        try {
            curObject = factory.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean throws exception on object: " + beanName);
        }

        if (!ScopeEnum.SINGLETON.equals(beanDefinition.scope())) {
            return curObject;
        }

        Object cachedObject = factoryBeanObjectCache.get(beanName);
        if (cachedObject != null) {
            return cachedObject == ClassUtils.NULL_OBJECT ? null : cachedObject;
        }

        factoryBeanObjectCache.put(beanName, curObject == null ? ClassUtils.NULL_OBJECT : curObject);
        return curObject;
    }


}
