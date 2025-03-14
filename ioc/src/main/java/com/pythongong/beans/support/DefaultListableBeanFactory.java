package com.pythongong.beans.support;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.ConfigurableListableBeanFactory;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.exception.BeansException;

public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new HashMap<>(256);
    
    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No bean named '" + beanName + "' is defined");
        }
        return beanDefinition;
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) {
        beanDefinitionMap.put(name, beanDefinition);
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

    
    
    
}
