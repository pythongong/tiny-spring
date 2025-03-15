package com.pythongong.context.support;

import java.util.Map;
import java.util.function.Supplier;

import com.pythongong.beans.BeanFactoryPostProcessor;
import com.pythongong.beans.ConfigurableListableBeanFactory;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.context.ConfigurableApplicationContext;
import com.pythongong.exception.BeansException;


public class GeneralApplicationContext implements ConfigurableApplicationContext {

    /**
     * function to refresh bean factory
     */
    private Runnable refreshBeanFactory;

    /**
     * function to get refreshed bean factory
     */
    private Supplier<ConfigurableListableBeanFactory> getBeanFactory;

    public GeneralApplicationContext(Runnable refreshBeanFactory,  Supplier<ConfigurableListableBeanFactory> getBeanFactory) {
        this.refreshBeanFactory = refreshBeanFactory;
        this.getBeanFactory = getBeanFactory;
    } 

    @Override
    public void refresh() throws BeansException {
       
        refreshBeanFactory.run();

        ConfigurableListableBeanFactory beanFactory = getBeanFactory.get();

        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        
        invokeBeanFactoryPostProcessors(beanFactory);


        registerBeanPostProcessors(beanFactory);

    
        beanFactory.preInstantiateSingletons();
    }

    

    @Override
    public void registerShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::close));
    }

    @Override
    public void close() {
        getBeanFactory.get().destroySingletons();
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
