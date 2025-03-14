package com.pythongong.beans.config;

public interface InitializingBean {
    
    void afterPropertiesSet() throws Exception;
}
