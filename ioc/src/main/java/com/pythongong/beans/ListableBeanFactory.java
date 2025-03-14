package com.pythongong.beans;

import java.util.Map;
import java.util.Set;

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

    /**
     * Return the names of all beans defined in this registry.
     *
     * 返回注册表中所有的Bean名称
     */
    Set<String> getBeanDefinitionNames();
    
}
