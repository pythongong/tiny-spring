package com.pythongong.beans.config;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PropertyValueList implements Iterable<PropertyValue>{
    
    private final List<PropertyValue> propertyValues;

    public PropertyValueList() {
        this.propertyValues = new ArrayList<>();
    }

    public void addPropertyValue(PropertyValue propertyValue) {
        propertyValues.add(propertyValue);
    }

    public PropertyValue getPropertyValue(String propertyName) {
        for (PropertyValue propertyValue : propertyValues) {
            if (propertyValue.name().equals(propertyName)) {
                return propertyValue;
            }
        }
        return null;
    }

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
