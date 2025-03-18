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
package com.pythongong.beans.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import lombok.EqualsAndHashCode;

/**
 * A collection of PropertyValue objects that holds bean property values.
 * This class provides methods to add and retrieve property values, and
 * implements Iterable to allow iteration over the property values.
 *
 * @author Cheng Gong
 * @since 2025-03-18
 */
@EqualsAndHashCode
public class PropertyValueList implements Iterable<PropertyValue> {
    
    private final List<PropertyValue> propertyValues;

    /**
     * Creates a new empty PropertyValueList.
     */
    public PropertyValueList() {
        this.propertyValues = new ArrayList<>();
    }

    /**
     * Adds a PropertyValue to this list.
     *
     * @param propertyValue the property value to add
     */
    public void addPropertyValue(PropertyValue propertyValue) {
        propertyValues.add(propertyValue);
    }

    /**
     * Returns the PropertyValue with the given name.
     *
     * @param propertyName the name of the property to find
     * @return the PropertyValue if found, null otherwise
     */
    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue propertyValue : propertyValues) {
            if (propertyValue.name().equals(propertyName)) {
                return propertyValue;
            }
        }
        return null;
    }

    /**
     * Returns an iterator over the property values in reverse order.
     *
     * @return an Iterator over the PropertyValue objects
     */
    @Override
    public Iterator<PropertyValue> iterator() {
        return new Iterator<PropertyValue>() {
            private int index = propertyValues.size() - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public PropertyValue next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return propertyValues.get(index--);
            }
        };
    }
}