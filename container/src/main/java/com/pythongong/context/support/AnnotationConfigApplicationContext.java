package com.pythongong.context.support;

import java.io.IOException;
import java.util.HashSet;
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
import com.pythongong.util.CheckUtils;
import com.pythongong.util.ClassPathSerchParam;
import com.pythongong.util.PathUtils;

public class AnnotationConfigApplicationContext implements ApplicationContext {

    private final Class<?> configurationClass;

    private DefaultListableBeanFactory beanFactory;

    private ApplicationEventMulticaster applicationEventMulticaster;

    public AnnotationConfigApplicationContext(Class<?> configurationClass) {
        CheckUtils.nullArgs(configurationClass, "AnnotationConfigApplicationContext receives null class");
        this.configurationClass = configurationClass;
        refresh();
    }

    @Override
    public void refresh() throws BeansException {

        this.beanFactory = new DefaultListableBeanFactory();
        
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
        Set<String> propertiesFiles = new HashSet<>();
        PathUtils.findClassPathFileNames(ClassPathSerchParam.builder()
        .packagePath(PathUtils.ROOT_CLASS_PATH)
        .searchSudDirect(false)
        .serachJar(false)
        .serachFile(true)
        .pathMapper((basePath, filePath) -> {
            String fileName =filePath.getFileName().toString();
            if (fileName.endsWith(PathUtils.PROPERTY_SUFFIX)) {
                propertiesFiles.add(fileName);
            }
        })
        .build());

        ResourceLoader resourceLoader = new DefaultResourceLoader();
        PropertyResolver propertyResolver = new PropertyResolver();
        propertiesFiles.forEach(propertiesFile -> {
            Resource resource = resourceLoader.getResource(PathUtils.CLASSPATH_URL_PREFIX + propertiesFile);
            try {
                propertyResolver.load(resource.getInputStream());
            } catch (IOException e) {
                throw new BeansException(String.format("Load propeties file {%s} failed", propertiesFile), e);
            }
        });
        return propertyResolver;
    }


    private void refreshBeanFactory() throws BeansException {
        ConfigurableClassParser parser = new ConfigurableClassParser(createPropertyResolver());
        Set<BeanDefinition> beanDefinitions = parser.parse(configurationClass);
        beanDefinitions.forEach(this.beanFactory::registerBeanDefinition);
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
