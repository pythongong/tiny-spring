package com.pythongong.context.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import com.pythongong.exception.BeansException;


public class GeneralApplicationEventMulticaster implements ApplicationEventMulticaster {

    // As it's related to remove operation, it's better to use linked list
    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    @SuppressWarnings("unchecked")
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        applicationListeners.forEach(applicationListener -> {
            if (isInvalidEvent(applicationListener, event)) {
                applicationListener.onApplicationEvent(event);
            }
        });
    }

    @SuppressWarnings("rawtypes")
    private boolean isInvalidEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) {
        Class<? extends ApplicationListener> listenerClass = applicationListener.getClass();
        Type genericType = listenerClass.getGenericInterfaces()[0];
        Type actulType = ((ParameterizedType) genericType).getActualTypeArguments()[0];
        String listenerClassName = actulType.getTypeName();
        try {
            Class<?> eventClass = Class.forName(listenerClassName);
            return event.getClass().isAssignableFrom(eventClass);
        } catch (ClassNotFoundException e) {
            throw new BeansException("wrong event type: " + listenerClassName);
        }
    }

    
}
