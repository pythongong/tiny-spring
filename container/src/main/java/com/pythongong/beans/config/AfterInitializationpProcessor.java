package com.pythongong.beans.config;

import com.pythongong.exception.BeansException;

@FunctionalInterface
public interface AfterInitializationpProcessor {

    /**
     * Apply this post processor after initialization of the given bean instance.
     *
     * @param bean     the new bean instance
     * @param beanName the name of the bean
     * @return the bean instance to use, either the original or a wrapped one
     * @throws BeansException in case of errors
     */
    void postProcessAfterInitialization(Object bean, String beanName) throws BeansException;

}
