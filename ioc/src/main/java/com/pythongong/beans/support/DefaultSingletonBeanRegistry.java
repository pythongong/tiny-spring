
package com.pythongong.beans.support;

import java.util.HashMap;
import java.util.Map;

import com.pythongong.DisposableBean;
import com.pythongong.beans.SingletonBeanRegistry;

public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry{

    private final Map<String, Object> singletonObjects = new HashMap<>();

    private final Map<String, DisposableBean> disposableBeanMap = new HashMap<>();

    public void registerDisposableBean(String beanName, DisposableBean bean) {
        disposableBeanMap.put(beanName, bean);
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonObjects.get(beanName);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName, singletonObject);
    }

    @Override
    public void destroySingletons() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'destroySingletons'");
    }

    
}