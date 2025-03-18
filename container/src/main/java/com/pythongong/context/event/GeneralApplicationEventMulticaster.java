/*
 * Copyright 2025 Cheng Gong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.pythongong.context.event;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.LinkedHashSet;
import java.util.Set;

import com.pythongong.exception.BeansException;

/**
 * Standard implementation of the ApplicationEventMulticaster interface.
 * Provides support for subscribing listeners and publishing events to them.
 * Uses reflection to check event types and ensure type-safety.
 *
 * @author Cheng Gong
 */
public class GeneralApplicationEventMulticaster implements ApplicationEventMulticaster {

    /**
     * Collection of application listeners, using LinkedHashSet for predictable iteration
     * order and efficient removal operations
     */
    public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new LinkedHashSet<>();

    /**
     * Adds an application listener to receive all events.
     *
     * @param listener the listener to add
     */
    @SuppressWarnings("unchecked")
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
    }

    /**
     * Removes an application listener so it no longer receives events.
     *
     * @param listener the listener to remove
     */
    @Override
    public void removeApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.remove(listener);
    }

    /**
     * Multicast the given event to all appropriate listeners.
     *
     * @param event the event to multicast
     */
    @Override
    public void multicastEvent(ApplicationEvent event) {
        applicationListeners.forEach(applicationListener -> {
            if (isInvalidEvent(applicationListener, event)) {
                applicationListener.onApplicationEvent(event);
            }
        });
    }

    /**
     * Determines whether an event should be handled by a specific listener
     * by checking the event type against the listener's generic type parameter.
     *
     * @param applicationListener the listener to check
     * @param event the event to check
     * @return true if the listener can handle the event
     * @throws BeansException if the event type cannot be determined
     */
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
