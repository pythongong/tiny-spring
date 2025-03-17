package com.pythongong.beans;

public interface SingletonBeanRegistry {
    
    Object getSingleton(String beanName);

     /**
     * 销毁单例对象
     */
    void destroySingletons();
}
