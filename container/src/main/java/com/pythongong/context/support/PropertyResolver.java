package com.pythongong.context.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

public class PropertyResolver {

    private final static String START = "${";

    private final static String END = "}";

    private Properties properties;

    public PropertyResolver() {
        this(null);
    }
    
    public PropertyResolver(Properties properties) {
        this.properties = properties == null ? new Properties() : properties;
        this.properties.putAll(System.getenv());
    }

    public void load(InputStream inputStream) throws IOException {
        properties.load(inputStream);
    }

    public String getProperty(String key) {
        PropertyExpr propertyExpr = parsePropertyExpr(key);
        String value = getProperty(key);
        if (propertyExpr != null && propertyExpr.defaultValue() != null) {
            value = properties.getProperty(key, propertyExpr.defaultValue());
        } 
        
        if (value == null) {
            throw new NoSuchElementException( key + " doesn't exist: ");
        }

        return value;
    }

    private PropertyExpr parsePropertyExpr(String key) {
        if (!key.startsWith(START) || !key.endsWith(END)) {
            return null;
        }
        int defaultValueIndex = key.indexOf(":");
        if (defaultValueIndex == -1) {
            key =  key.substring(START.length(), key.length() - END.length());
            return new PropertyExpr(key, null);
        } 

        String defaultValue = key.substring(defaultValueIndex + 1, key.length() - END.length());
        key = key.substring(START.length(), defaultValueIndex);
        return new PropertyExpr(key, defaultValue);
    }
}

record PropertyExpr(String key, String defaultValue) {
}
