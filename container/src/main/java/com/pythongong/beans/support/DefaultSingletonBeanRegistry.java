
package com.pythongong.beans.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.SingletonBeanRegistry;
import com.pythongong.beans.config.DisposableBean;
import com.pythongong.exception.BeansException;
import com.pythongong.exception.NoScuhBeanException;
import com.pythongong.util.CheckUtils;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry{

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<String, DisposableBean> disposableBeanMap = new HashMap<>();

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        CheckUtils.emptyString(beanName, "DefaultSingletonBeanRegistry.registerDisposableBean receives empty bean name");
        CheckUtils.nullArgs(bean, "DefaultSingletonBeanRegistry.registerDisposableBean receives null bean");
        disposableBeanMap.put(beanName, bean);
    }

    @Override
    public Object getSingleton(String beanName) {
        CheckUtils.emptyString(beanName, "DefaultSingletonBeanRegistry.getSingleton receives empty bean name");
        Object bean = singletonObjects.get(beanName);
        return bean;
    }


    @Override
    public void destroySingletons() {
        Object[] disaposableNames = disposableBeanMap.keySet().toArray();

        Arrays.stream(disaposableNames).forEach(beanName -> {
            DisposableBean bean = disposableBeanMap.get(beanName);
            if (bean == null) {
                throw new NoScuhBeanException((String) beanName,  DisposableBean.class);
            }
            disposableBeanMap.remove(beanName);
            try {
                bean.destroy();
            } catch (Exception e) {
                throw new BeansException(String.format("Fail to destory {%s} bean", beanName), e);
            }
        });
        
    }

    protected void registerSingleton(String beanName, Object bean) {
        CheckUtils.emptyString(beanName, "DefaultSingletonBeanRegistry.registerSingleton receives empty bean name");
        CheckUtils.nullArgs(bean, "DefaultSingletonBeanRegistry.registerSingleton receives null bean");
        singletonObjects.put(beanName, bean);
    }

    
}