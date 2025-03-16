package com.pythongong.beans.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.AutowireCapableBeanFactory;
import com.pythongong.beans.Aware;
import com.pythongong.beans.BeanClassLoaderAware;
import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.BeanFactoryAware;
import com.pythongong.beans.BeanNameAware;
import com.pythongong.beans.ConfigurableListableBeanFactory;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.config.BeanReference;
import com.pythongong.beans.config.DisposableBean;
import com.pythongong.beans.config.InitializingBean;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.context.event.ApplicationEventMulticaster;
import com.pythongong.context.event.GeneralApplicationEventMulticaster;
import com.pythongong.enums.ScopeEnum;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.AutoWired;
import com.pythongong.util.ClassUtils;

public class DefaultListableBeanFactory implements BeanDefinitionRegistry, ConfigurableListableBeanFactory, AutowireCapableBeanFactory {

    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiation();

    private final GeneralBeanFactory generalBeanFactory;


    private final DefaultSingletonBeanRegistry singletonBeanRegistry;
    
    
    public DefaultListableBeanFactory() {
        generalBeanFactory = new GeneralBeanFactory(this::getBeanDefinition, this::createBean);
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
            if (type.isAssignableFrom(beanDefinition.beanClass())) {
                results.put(beanName, (T) getBean(beanName));
            }
        });

        if (results.isEmpty()) {
            throw new NoSuchElementException("no bean of required type: " + type.getName());
        }

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
        return this.generalBeanFactory.getBean(name);
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

    @Override
    public ApplicationEventMulticaster initApplicationEventMulticaster() {
        GeneralApplicationEventMulticaster applicationEventMulticaster = new GeneralApplicationEventMulticaster();
        generalBeanFactory.getSingletonBeanRegistry().addSingleton(ClassUtils.APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
        return applicationEventMulticaster;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
         Object result = existingBean;
         for (BeanPostProcessor processor : generalBeanFactory.getBeanPostProcessors()) {
             Object current = processor.postProcessBeforeInitialization(result, beanName);
             if (null == current) return result;
             result = current;
         }
         return result;
    }

    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : generalBeanFactory.getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
    }

    protected Object createBean(BeanDefinition beanDefinition) throws BeansException {
        Object bean;
         try {
            bean = createBeanInstance(beanDefinition);
            fillPropertyValues(beanDefinition, bean);
            bean = initializeBean(bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Initiation of bean failed: " + beanDefinition.beanName(), e);
        }
        registerDisposableBeanIfNecessary(bean, beanDefinition);
        if (ScopeEnum.SINGLETON.equals(beanDefinition.scope())) {
            singletonBeanRegistry.addSingleton( beanDefinition.beanName(), bean);
        }
        return bean;
    }

    private void registerDisposableBeanIfNecessary(Object bean, BeanDefinition beanDefinition) {
        if (!ScopeEnum.SINGLETON.equals(beanDefinition.scope())) {
            return;
        }
        if (bean instanceof DisposableBean || beanDefinition.destroyMethod() != null) {
            singletonBeanRegistry.registerDisposableBean(beanDefinition.beanName(), new DisposableBeanAdapter(bean, beanDefinition.destroyMethod()));
        }
    }

    private Object createBeanInstance(BeanDefinition beanDefinition) {
        Constructor<?> constructorToUse = beanDefinition.constructor();
        Class<?> beanClass = beanDefinition.beanClass();
        Class<?>[] parameterTypes = constructorToUse.getParameterTypes();
        Object[] args = new Object[parameterTypes.length];
        for (int i = 0; i < args.length; i++) {
            args[i] = getBeansOfType(parameterTypes[i]);
        }
        return instantiationStrategy.instance(beanClass, constructorToUse, args);
    }

    private void fillPropertyValues(BeanDefinition beanDefinition, Object bean) {
        PropertyValueList propertyValueList = beanDefinition.propertyValueList();
        if (propertyValueList == null) {
            return;
        }
        Class<?> beanClass = beanDefinition.beanClass();
        for (PropertyValue propertyValue : propertyValueList) {
            String propertyName = propertyValue.name();
            Object value = propertyValue.value();

            if (value instanceof BeanDefinition) {
                BeanReference beanReference = (BeanReference) value;
                value = generalBeanFactory.getBean(beanReference.getBeanName());
            }

            try {
                Field declaredField = beanClass.getDeclaredField(propertyName);
                declaredField.setAccessible(true);
                declaredField.set(bean, value);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                throw new BeansException("Error setting property values: " + beanClass.getName(), e);
            }
        }

    }

    private Object initializeBean(Object bean, BeanDefinition beanDefinition) {
        String beanName = beanDefinition.beanName();
        awareBean(beanName, bean);

        // BeanPostProcessor Before 
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // invoke initMethods 
        try {
            invokeInitMethods(beanName, wrappedBean, beanDefinition);
            methodInject(beanName, wrappedBean, beanDefinition);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // BeanPostProcessor After 
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return wrappedBean;
    }

    private void awareBean(String beanName, Object bean) {
        if (!(bean instanceof Aware)) {
            return;
        }

        if (bean instanceof BeanFactoryAware) {
            ((BeanFactoryAware) bean).setBeanFactory(this);
        }
        
        if (bean instanceof BeanClassLoaderAware){
            ((BeanClassLoaderAware) bean).setBeanClassLoader(ClassUtils.getDefaultClassLoader());
        }
        if (bean instanceof BeanNameAware) {
            ((BeanNameAware) bean).setBeanName(beanName);
        }
            
    }
    
    private void methodInject(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {
        Class<?> beanClass = beanDefinition.getClass();
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            AutoWired autoWired = method.getAnnotation(AutoWired.class);
            if (autoWired == null) {
                continue;
            }
            int count = method.getParameterCount();

            if (count == 0) {
                throw new BeansException("inject method has no arugment");
            }

            if (count > 1) {
                throw new BeansException("inject method has more than one arugment");
            }

            Object bean = generalBeanFactory.getBean(beanName);
            if (bean == null && autoWired.required()) {
                throw new BeansException("inject method has more than one arugment");
            }
            
            try {
                method.invoke(wrappedBean, bean);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new BeansException("bean of inject method is not found");
            }

            break;
            
        }
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) throws Exception {
        if (wrappedBean instanceof InitializingBean) {
            ((InitializingBean) wrappedBean).afterPropertiesSet();
            return;
        }


        Method initMethod = beanDefinition.initMethod();
        if (initMethod == null) {
            return;
        }

        initMethod.invoke(wrappedBean);
    }
    
}
