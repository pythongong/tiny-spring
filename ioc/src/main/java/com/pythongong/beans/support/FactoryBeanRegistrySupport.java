package com.pythongong.beans.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.FactoryBean;
import com.pythongong.exception.BeansException;
import com.pythongong.util.ClassUtils;

public class FactoryBeanRegistrySupport {

    private final Map<String, Object> factoryBeanObjectCache = new ConcurrentHashMap<>();

    protected Object getCachedObjectForFactoryBean(String beanName) {
        Object cachedObject = factoryBeanObjectCache.get(beanName);
        return cachedObject == ClassUtils.NULL_OBJECT ? null : cachedObject;
    }

    protected Object getObjectFromFactoryBean(FactoryBean<?> factory, String beanName) {
        Object curObject = null;
        try {
            curObject = factory.getObject();
        } catch (Exception e) {
            throw new BeansException("FactoryBean throws exception on object: " + beanName);
        }

        if (!factory.isSingleton()) {
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
