package com.pythongong.beans;

@FunctionalInterface
public interface BeanNameAware extends Aware {

    void setBeanName(String name);
    
}
