
package com.pythongong.beans.support;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.SingletonBeanRegistry;
import com.pythongong.beans.config.DisposableBean;
import com.pythongong.exception.BeansException;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry{

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    private final Map<String, DisposableBean> disposableBeanMap = new HashMap<>();

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeanMap.put(beanName, bean);
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }


    @Override
    public void destroySingletons() {
        Object[] disaposableNames = disposableBeanMap.keySet().toArray();

        Arrays.stream(disaposableNames).forEach(beanName -> {
            DisposableBean bean = disposableBeanMap.get(beanName);
            if (bean == null) {
                throw new BeansException("Disaposable bean is null");
            }
            disposableBeanMap.remove(beanName);
            try {
                bean.destroy();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    
}