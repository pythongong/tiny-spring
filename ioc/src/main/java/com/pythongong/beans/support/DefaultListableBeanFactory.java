package com.pythongong.beans.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.ConfigurableListableBeanFactory;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.exception.BeansException;

public class DefaultListableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(256);

    private final GeneralAutowireCapableBeanFactory generalCapableBeanFactory;

    private final GeneralBeanFactory generalBeanFactory;

    private final DefaultSingletonBeanRegistry singletonBeanRegistry;
    
    
    public DefaultListableBeanFactory() {
        generalCapableBeanFactory = new GeneralAutowireCapableBeanFactory((beanName) -> getBeanDefinition(beanName));
        generalBeanFactory = generalCapableBeanFactory.getGeneralBeanFactory();
        singletonBeanRegistry = generalBeanFactory.getSingletonBeanRegistry();
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean named '" + beanName + "' is defined");
        }
        return beanDefinition;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) {
        beanDefinitionMap.put(beanDefinition.beanName(), beanDefinition);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        Map<String, T> results = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            if (beanDefinition.beanClass().isAssignableFrom(type)) {
                results.put(beanName, (T) getBean(beanName));
            }
        });

        return results;
    }

    @Override
    public Set<String> getBeanDefinitionNames() {
        return beanDefinitionMap.keySet();
    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        beanDefinitionMap.keySet().forEach(this::getBean);
    }

    @Override
    public Object getBean(String name) throws BeansException {
        return this.generalCapableBeanFactory.getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return this.generalCapableBeanFactory.getBean(name);
    }

    @Override
    public Object getSingleton(String beanName) {
        return singletonBeanRegistry.getSingleton(beanName);
    }

    @Override
    public void destroySingletons() {
        singletonBeanRegistry.destroySingletons();
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        generalBeanFactory.addBeanPostProcessor(beanPostProcessor);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return generalBeanFactory.getBean(name, requiredType);
    }

    
    
    
}
