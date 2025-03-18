package com.pythongong.context;

import com.pythongong.beans.factory.ListableBeanFactory;
import com.pythongong.context.event.ApplicationEventPublisher;
import com.pythongong.exception.BeansException;

public interface ApplicationContext extends ListableBeanFactory, ApplicationEventPublisher {

    void refresh() throws BeansException;

    void registerShutdownHook();

    void close();
    
    
}
