package com.pythongong.context;

import com.pythongong.beans.Aware;
import com.pythongong.exception.BeansException;

@FunctionalInterface
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
    
}
