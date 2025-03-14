package com.pythongong.beans.config;

@FunctionalInterface
public interface InitializingBean {
    
    void afterPropertiesSet() throws Exception;
}
