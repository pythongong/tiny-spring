package com.pythongong.beans.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

import com.pythongong.beans.AutowireCapableBeanFactory;
import com.pythongong.beans.Aware;
import com.pythongong.beans.BeanClassLoaderAware;
import com.pythongong.beans.BeanFactoryAware;
import com.pythongong.beans.BeanNameAware;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.config.BeanReference;
import com.pythongong.beans.config.DisposableBean;
import com.pythongong.beans.config.InitializingBean;
import com.pythongong.beans.config.MetaData;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.AutoWired;
import com.pythongong.util.ClassUtils;

public class GeneralAutowireCapableBeanFactory implements AutowireCapableBeanFactory {

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiation();

    private final GeneralBeanFactory generalBeanFactory;

    private final DefaultSingletonBeanRegistry singletonBeanRegistry;


    public GeneralBeanFactory getGeneralBeanFactory() {
        return generalBeanFactory;
    }

    public GeneralAutowireCapableBeanFactory(Function<String, BeanDefinition> getBeanDefinition) {
        generalBeanFactory = new GeneralBeanFactory(getBeanDefinition, (metaData) -> createBean(metaData));
        singletonBeanRegistry = generalBeanFactory.getSingletonBeanRegistry();
    }
    


    protected Object createBean(MetaData metaData) throws BeansException {
        BeanDefinition beanDefinition = metaData.beanDefinition();
        String beanName = metaData.beanName();
        Object bean;
         try {
            bean = createBeanInstance(beanDefinition, metaData.args());
            fillPropertyValues(beanDefinition, bean);
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Initiation of bean failed", e);
        }
        registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);
        singletonBeanRegistry.addSingleton(beanName, bean);
        return bean;
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

    private void registerDisposableBeanIfNecessary(String beanName, Object bean, BeanDefinition beanDefinition) {
        if (bean instanceof DisposableBean || beanDefinition.destroyMethod() != null) {
            singletonBeanRegistry.registerDisposableBean(beanName, new DisposableBeanAdapter(bean, beanDefinition.destroyMethod()));
        }
    }

    private Object createBeanInstance(BeanDefinition beanDefinition, Object[] args) {
        Constructor<?> constructorToUse = null;
        Class<?> beanClass = beanDefinition.beanClass();
        Constructor<?>[] declaredConstructors = beanClass.getDeclaredConstructors();
        for (Constructor<?> constructor : declaredConstructors) {
            if (args != null && constructor.getParameterTypes().length == args.length) {
                constructorToUse = constructor;
                break;
            }
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

    private Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) {
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

        // 2. BeanPostProcessor After 
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

    @Override
    public Object getBean(String name) throws BeansException {
        return this.generalBeanFactory.getBean(name);
    }

    @Override
    public Object getBean(String name, Object... args) throws BeansException {
        return this.generalBeanFactory.getBean(name, args);
    }
    
    
}
