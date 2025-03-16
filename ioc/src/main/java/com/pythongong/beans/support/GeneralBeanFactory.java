package com.pythongong.beans.support;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.pythongong.beans.ConfigurableBeanFactory;
import com.pythongong.beans.FactoryBean;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.exception.BeansException;

@SuppressWarnings("unchecked")
public class GeneralBeanFactory implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final Function<String, BeanDefinition> getBeanDefinition;

    private final Function<BeanDefinition, Object> createBean;

    private final DefaultSingletonBeanRegistry  singletonBeanRegistry = new DefaultSingletonBeanRegistry();

    private final FactoryBeanRegistrySupport beanRegistrySupport = new FactoryBeanRegistrySupport();

    public DefaultSingletonBeanRegistry getSingletonBeanRegistry() {
        return singletonBeanRegistry;
    }

    public GeneralBeanFactory(Function<String, BeanDefinition> getBeanDefinition,
            Function<BeanDefinition, Object> createBean) {
        this.getBeanDefinition = getBeanDefinition;
        this.createBean = createBean;
    }
    

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name);
    }
    
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        if (!beanPostProcessors.contains(beanPostProcessor)) {
            beanPostProcessors.add(beanPostProcessor);
        }

    }

     /**
     * Return the list of BeanPostProcessors that will get applied
     * to beans created with this factory.
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException {
        return doGetBean(name);
    }

    private <T> T doGetBean(String beanname) {
        Object sharedInstance = singletonBeanRegistry.getSingleton(beanname);
        if (sharedInstance != null && !(sharedInstance instanceof FactoryBean)) {
            return (T) sharedInstance;
        }
        BeanDefinition beanDefinition = getBeanDefinition.apply(beanname);
        if (sharedInstance != null) {
            return (T) getObjectForBeanInstance(sharedInstance, beanDefinition);
        }
        return (T) createBean.apply(beanDefinition);
    }

    private Object getObjectForBeanInstance(Object sharedInstance, BeanDefinition beanDefinition) {
            Object cachedObject = beanRegistrySupport.getCachedObjectForFactoryBean(beanDefinition.beanName());
            if (cachedObject == null) {
                cachedObject = beanRegistrySupport.getObjectFromFactoryBean((FactoryBean<?>) sharedInstance, beanDefinition);
        }
        return cachedObject;
    }
    
}
