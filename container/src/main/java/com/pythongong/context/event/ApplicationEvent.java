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

import java.util.EventObject;

/**
 * Base class for all application events in the framework.
 * All events in the application context must extend this class.
 *
 * @author Cheng Gong
 */
public class ApplicationEvent extends EventObject {

    /**
     * Creates a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
