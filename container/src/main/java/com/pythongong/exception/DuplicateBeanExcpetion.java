package com.pythongong.exception;

public class DuplicateBeanExcpetion extends BeansException {

    public DuplicateBeanExcpetion(String msg) {
        super(msg);
    }
    
    public DuplicateBeanExcpetion(String beanName, Class<?> beanClass) {
        this(String.format("Duplicate bean named: {%s}, type: {%s}", beanName, beanClass.getName()));
    }
}
