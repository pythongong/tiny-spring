package com.pythongong.context.support;

import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

import com.pythongong.beans.BeanFactoryPostProcessor;
import com.pythongong.beans.ConfigurableListableBeanFactory;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.context.ConfigurableApplicationContext;
import com.pythongong.exception.BeansException;


public class GeneralApplicationContext implements ConfigurableApplicationContext {

    private Runnable refreshBeanFactory;

    private Supplier<ConfigurableListableBeanFactory> getBeanFactory;

    public GeneralApplicationContext(Runnable refreshBeanFactory,  Supplier<ConfigurableListableBeanFactory> getBeanFactory) {
        this.refreshBeanFactory = refreshBeanFactory;
        this.getBeanFactory = getBeanFactory;
    } 

    @Override
    public void refresh() throws BeansException {
        // 1. 创建 BeanFactory，并加载 BeanDefinition
        refreshBeanFactory.run();

        // 2. 获取 BeanFactory
        ConfigurableListableBeanFactory beanFactory = getBeanFactory.get();

        // 3. 在 Bean 实例化之前，执行 BeanFactoryPostProcessor (Invoke factory processors registered as beans in the context.)
        invokeBeanFactoryPostProcessors(beanFactory);

        // 4. BeanPostProcessor 需要提前于其他 Bean 对象实例化之前执行注册操作
        registerBeanPostProcessors(beanFactory);

        // 5. 提前实例化单例Bean对象
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
