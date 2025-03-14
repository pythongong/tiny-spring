package com.pythongong.beans.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import com.pythongong.beans.AutowireCapableBeanFactory;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.beans.config.BeanReference;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.exception.BeansException;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory{

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiation();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws BeansException {
         Object bean;
         try {
            bean = createBeanInstance(beanDefinition, args);
            fillPropertyValues(beanDefinition, bean);
            bean = initializeBean(beanName, bean, beanDefinition);
        } catch (Exception e) {
            throw new BeansException("Initiation of bean failed", e);
        }
        addSingleton(beanName, bean);
         return bean;
    }

    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName) throws BeansException {
         Object result = existingBean;
         for (BeanPostProcessor processor : getBeanPostProcessors()) {
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
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (null == current) return result;
            result = current;
        }
        return result;
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
                value = getBean(beanReference.getBeanName());
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
        // 1. 执行 BeanPostProcessor Before 处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 待完成内容：invokeInitMethods(beanName, wrappedBean, beanDefinition);
        invokeInitMethods(beanName, wrappedBean, beanDefinition);

        // 2. 执行 BeanPostProcessor After 处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        return wrappedBean;
    }

    private void invokeInitMethods(String beanName, Object wrappedBean, BeanDefinition beanDefinition) {

    }
    
    
    
}
