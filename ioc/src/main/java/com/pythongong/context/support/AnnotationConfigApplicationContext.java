package com.pythongong.context.support;

import java.util.Map;
import java.util.Set;

import com.pythongong.beans.ConfigurableListableBeanFactory;
import com.pythongong.beans.ListableBeanFactory;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.context.ConfigurableApplicationContext;
import com.pythongong.context.annotation.ConfigurationClassParser;
import com.pythongong.exception.BeansException;

public class AnnotationConfigApplicationContext implements ConfigurableApplicationContext, ListableBeanFactory {

    private final Class<?> configurationClass;

    private DefaultListableBeanFactory beanFactory;

    private GeneralApplicationContext applicationContext;

    public AnnotationConfigApplicationContext(Class<?> configurationClass) {
        this.configurationClass = configurationClass;
        applicationContext = new GeneralApplicationContext(() -> refreshBeanFactory(), () -> getBeanFactory());
    }

    @Override
    public void refresh() throws BeansException {
        applicationContext.refresh();
    }

    @Override
    public void registerShutdownHook() {
        applicationContext.registerShutdownHook();
    }

    @Override
    public void close() {
        applicationContext.close();
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return this.beanFactory.getBeansOfType(type);
    }

    @Override
    public Set<String> getBeanDefinitionNames() {
        return this.beanFactory.getBeanDefinitionNames();
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return this.beanFactory.getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return this.beanFactory.getBean(name, args);
    }


    private void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        ConfigurationClassParser parser = new ConfigurationClassParser(beanFactory);
        parser.parse(configurationClass);
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }


    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
    
}
