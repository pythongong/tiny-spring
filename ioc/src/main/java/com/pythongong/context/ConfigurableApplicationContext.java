package com.pythongong.context;

import com.pythongong.exception.BeansException;

public interface ConfigurableApplicationContext  extends ApplicationContext {

    /**
     * Refresh the container
     *
     * @throws BeansException
     */
    void refresh() throws BeansException;

    void registerShutdownHook();

    void close();
    
}
