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

import com.pythongong.context.ApplicationContext;

/**
 * Base class for events raised by an ApplicationContext.
 * These events will get published to all registered ApplicationListeners.
 *
 * @author Cheng Gong
 */
public class ApplicationContextEvent extends ApplicationEvent {

    /**
     * Creates a new ApplicationContextEvent.
     *
     * @param source the ApplicationContext that the event is raised for
     */
    public ApplicationContextEvent(Object source) {
        super(source);
    }

    /**
     * Gets the ApplicationContext that published this event.
     *
     * @return the ApplicationContext that published this event
     */
    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }
}
