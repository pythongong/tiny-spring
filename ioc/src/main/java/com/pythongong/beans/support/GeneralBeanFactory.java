package com.pythongong.beans.support;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.pythongong.beans.ConfigurableBeanFactory;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.config.MetaData;
import com.pythongong.exception.BeansException;

public class GeneralBeanFactory implements ConfigurableBeanFactory {

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    private final Function<String, BeanDefinition> getBeanDefinition;

    private final Function<MetaData, Object> createBean;

    private final DefaultSingletonBeanRegistry  singletonBeanRegistry = new DefaultSingletonBeanRegistry();

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

    private Object doGetBean(String beanname, Object[] args) {
        Object bean = singletonBeanRegistry.getSingleton(beanname);
        if (bean != null) {
            return bean;
        }
        BeanDefinition beanDefinition = getBeanDefinition.apply(beanname);
        return createBean.apply(new MetaData(beanname, beanDefinition, args));
    }

     /**
     * Return the list of BeanPostProcessors that will get applied
     * to beans created with this factory.
     */
    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }
    
}
