package com.pythongong.beans;

public interface BeanClassLoaderAware extends Aware {
    
    void setBeanClassLoader(ClassLoader classLoader);

}
