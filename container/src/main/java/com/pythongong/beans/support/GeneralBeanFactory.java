package com.pythongong.beans.support;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.pythongong.beans.ConfigurableBeanFactory;
import com.pythongong.beans.FactoryBean;
import com.pythongong.beans.SingletonBeanRegistry;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.exception.BeansException;
import com.pythongong.exception.NoScuhBeanException;
import com.pythongong.util.CheckUtils;

@SuppressWarnings("unchecked")
public class GeneralBeanFactory implements ConfigurableBeanFactory {

    private final Function<String, BeanDefinition> getBeanDefinition;

    private final Function<BeanDefinition, Object> createBean;

    private final SingletonBeanRegistry  singletonBeanRegistry;

    private final FactoryBeanRegistrySupport beanRegistrySupport = new FactoryBeanRegistrySupport();

    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public GeneralBeanFactory(Function<String, BeanDefinition> getBeanDefinition,
            Function<BeanDefinition, Object> createBean, SingletonBeanRegistry singletonBeanRegistry) {
        
        CheckUtils.nullArgs(singletonBeanRegistry, "GeneralBeanFactory recevies null SingletonBeanRegistry");
        CheckUtils.nullArgs(createBean, "GeneralBeanFactory recevies null createBean function");
        CheckUtils.nullArgs(getBeanDefinition, "GeneralBeanFactory recevies null getBeanDefinition function");

        this.getBeanDefinition = getBeanDefinition;
        this.createBean = createBean;
        this.singletonBeanRegistry = singletonBeanRegistry;
    }
    

    @Override
    public Object getBean(String beanName) throws BeansException {
        CheckUtils.emptyString(beanName, "GeneralBeanFactory.getBean recevies empty bean name");
        return doGetBean(beanName);
    }
    
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        CheckUtils.nullArgs(beanPostProcessor, "GeneralBeanFactory.addBeanPostProcessor recevies null processor");
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
        CheckUtils.emptyString(name, "GeneralBeanFactory.getBean recevies empty bean name");
        CheckUtils.nullArgs(requiredType, "GeneralBeanFactory.getBean recevies null bean type");
        return doGetBean(name);
    }

    private <T> T doGetBean(String beanName) {
        Object sharedInstance = singletonBeanRegistry.getSingleton(beanName);
        if (sharedInstance != null && !(sharedInstance instanceof FactoryBean)) {
            return (T) sharedInstance;
        }
        BeanDefinition beanDefinition = getBeanDefinition.apply(beanName);
        if (beanDefinition == null) {
            throw new NoScuhBeanException(String.format("No bean is definited as: {%s}", beanName));
        }

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
