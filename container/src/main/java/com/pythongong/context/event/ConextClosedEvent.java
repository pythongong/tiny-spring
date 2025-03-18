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

/**
 * Event raised when an ApplicationContext is closed.
 * This event is published immediately before the context is closed,
 * allowing listeners to perform any necessary cleanup operations.
 *
 * @author Cheng Gong
 */
public class ConextClosedEvent extends ApplicationEvent {

    /**
     * Create a new ConextClosedEvent.
     *
     * @param source the ApplicationContext that is being closed
     */
    public ConextClosedEvent(Object source) {
        super(source);
    }
}
