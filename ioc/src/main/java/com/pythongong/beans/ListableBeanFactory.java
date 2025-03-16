package com.pythongong.beans;

import java.util.Map;
import com.pythongong.exception.BeansException;

public interface ListableBeanFactory extends BeanFactory {

    /**
     * 
     * @param type
     * @param <T>
     * @return
     * @throws BeansException
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
    
}
