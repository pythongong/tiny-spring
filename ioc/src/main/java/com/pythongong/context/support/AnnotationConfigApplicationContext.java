package com.pythongong.context.support;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.pythongong.beans.ConfigurableListableBeanFactory;
import com.pythongong.beans.ListableBeanFactory;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.context.ConfigurableApplicationContext;
import com.pythongong.context.annotation.ConfigurationClassParser;
import com.pythongong.core.io.DefaultResourceLoader;
import com.pythongong.core.io.Resource;
import com.pythongong.core.io.ResourceLoader;
import com.pythongong.exception.BeansException;
import com.pythongong.util.PathUtils;

public class AnnotationConfigApplicationContext implements ConfigurableApplicationContext, ListableBeanFactory {

    private final Class<?> configurationClass;

    private DefaultListableBeanFactory beanFactory;

    private GeneralApplicationContext applicationContext;

    private final PropertyResolver propertyResolver;

    public AnnotationConfigApplicationContext(Class<?> configurationClass) {
        this.configurationClass = configurationClass;
        applicationContext = new GeneralApplicationContext(() -> refreshBeanFactory(), () -> getBeanFactory());
        propertyResolver = createPropertyResolver();
        beanFactory = new DefaultListableBeanFactory();
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
        ConfigurationClassParser parser = new ConfigurationClassParser(propertyResolver, beanFactory);
        Set<BeanDefinition> beanDefinitions = parser.parse(configurationClass);
        beanDefinitions.forEach(beanDefinition -> {
            beanFactory.registerBeanDefinition(beanDefinition);
        });
        this.beanFactory = beanFactory;
    }

    private DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }


    public ConfigurableListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
    
}
