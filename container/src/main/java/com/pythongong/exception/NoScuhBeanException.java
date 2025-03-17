package com.pythongong.exception;

public class NoScuhBeanException extends BeansException {

    public NoScuhBeanException(String msg) {
        super(msg);
    }

    public NoScuhBeanException(Object beanName, Class<?> beanClass) {
        this(String.format("No bean named: {%s}, type: {%s}", beanName, beanClass.getName()));
    }
    
    public NoScuhBeanException(Class<?> beanType) {
        this(String.format("No bean as type: {%s}", beanType.getName()));
    }
    
}
