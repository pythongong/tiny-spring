package com.pythongong.context.support;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.pythongong.beans.BeanFactoryPostProcessor;
import com.pythongong.beans.ConfigurableListableBeanFactory;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.context.ApplicationContext;
import com.pythongong.context.annotation.ConfigurableClassParser;
import com.pythongong.context.event.ApplicationEvent;
import com.pythongong.context.event.ApplicationEventMulticaster;
import com.pythongong.context.event.ApplicationListener;
import com.pythongong.context.event.ConextClosedEvent;
import com.pythongong.context.event.ContextRefreshedEvent;
import com.pythongong.core.io.DefaultResourceLoader;
import com.pythongong.core.io.Resource;
import com.pythongong.core.io.ResourceLoader;
import com.pythongong.exception.BeansException;
import com.pythongong.util.PathUtils;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final Class<?> configurationClass;

    private DefaultListableBeanFactory beanFactory;

    private final PropertyResolver propertyResolver;

    private ApplicationEventMulticaster applicationEventMulticaster;

    public AnnotationConfigApplicationContext(Class<?> configurationClass) {
        this.configurationClass = configurationClass;
        propertyResolver = createPropertyResolver();
        refresh();
    }

    @Override
    public void refresh() throws BeansException {
        
        refreshBeanFactory();

        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        
        invokeBeanFactoryPostProcessors(beanFactory);


        registerBeanPostProcessors(beanFactory);

        applicationEventMulticaster = beanFactory.initApplicationEventMulticaster();

        beanFactory.preInstantiateSingletons();

        registerListeners();

        publishEvent(new ContextRefreshedEvent(this));
    }

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        publishEvent(new ConextClosedEvent(this));
        beanFactory.destroySingletons();
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

    @SuppressWarnings("rawtypes")
    private void registerListeners() {
        Map<String, ApplicationListener> listenerMap = beanFactory.getBeansOfType(ApplicationListener.class);
        listenerMap.forEach((name, lister) -> {
            applicationEventMulticaster.addApplicationListener(lister);
        });
    }

    private PropertyResolver createPropertyResolver() {
        Set<String> fileNames = PathUtils.getFileNamesOfPackage(configurationClass.getPackageName(), (fileName) -> {
            if (fileName.endsWith(PathUtils.PROPERTY_SUFFIX)) {
                return fileName;
            }
            return null;
        });
        ResourceLoader resourceLoader = new DefaultResourceLoader();
        PropertyResolver propertyResolver = new PropertyResolver();
        if (fileNames.isEmpty()) {
            return propertyResolver;
        }
        fileNames.forEach(fileName -> {
            Resource resource = resourceLoader.getResource(fileName);
            try {
                propertyResolver.load(resource.getInputStream());
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        return propertyResolver;
    }


    private void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        ConfigurableClassParser parser = new ConfigurableClassParser(propertyResolver);
        Set<BeanDefinition> beanDefinitions = parser.parse(configurationClass);
        beanDefinitions.forEach(beanDefinition -> {
            beanFactory.registerBeanDefinition(beanDefinition);
        });
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return beanFactory.getBean(name, requiredType);
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }
    
}
