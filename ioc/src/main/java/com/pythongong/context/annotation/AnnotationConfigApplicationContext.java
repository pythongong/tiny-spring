package com.pythongong.context.annotation;

import com.pythongong.beans.impl.DefaultListableBeanFactory;

public class AnnotationConfigApplicationContext {

    private final ClassPathBeanDefinitionScanner scanner;
    

    public AnnotationConfigApplicationContext() {
        this.scanner = new ClassPathBeanDefinitionScanner(new DefaultListableBeanFactory());
    }

    
    
}
