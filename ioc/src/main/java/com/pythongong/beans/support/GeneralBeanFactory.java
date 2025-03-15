package com.pythongong.beans.support;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.pythongong.beans.ConfigurableBeanFactory;
import com.pythongong.beans.FactoryBean;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.config.MetaData;
import com.pythongong.exception.BeansException;

@SuppressWarnings("unchecked")
public class GeneralBeanFactory implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final Function<String, BeanDefinition> getBeanDefinition;

    private final Function<MetaData, Object> createBean;

    private final DefaultSingletonBeanRegistry  singletonBeanRegistry = new DefaultSingletonBeanRegistry();

    private final FactoryBeanRegistrySupport beanRegistrySupport = new FactoryBeanRegistrySupport();

    public DefaultSingletonBeanRegistry getSingletonBeanRegistry() {
        return singletonBeanRegistry;
    }

    public GeneralBeanFactory(Function<String, BeanDefinition> getBeanDefinition,
            Function<MetaData, Object> createBean) {
        this.getBeanDefinition = getBeanDefinition;
        this.createBean = createBean;
    }
    

    @Override
    public Object getBean(String name) throws BeansException {
        return doGetBean(name, null);
    }
    

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return doGetBean(name, args);
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        if (!beanPostProcessors.contains(beanPostProcessor)) {
            beanPostProcessors.add(beanPostProcessor);
        }

    }

    private <T> T doGetBean(String beanname, Object[] args) {
        Object sharedInstance = singletonBeanRegistry.getSingleton(beanname);
        if (sharedInstance != null) {
            return (T) getObjectForBeanInstance(sharedInstance, beanname);
        }
        BeanDefinition beanDefinition = getBeanDefinition.apply(beanname);
        return (T) createBean.apply(new MetaData(beanname, beanDefinition, args));
    }

    private Object getObjectForBeanInstance(Object sharedInstance, String beanname) {
        if (!(sharedInstance instanceof FactoryBean)) {
            return sharedInstance;
        }
        Object cachedObject = beanRegistrySupport.getCachedObjectForFactoryBean(beanname);
        if (cachedObject == null) {
            cachedObject = beanRegistrySupport.getObjectFromFactoryBean((FactoryBean<?>) sharedInstance, beanname);
        }
        return cachedObject;
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
        return doGetBean(name, null);
    }
    
}
