package com.pythongong.beans.impl;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import com.pythongong.beans.InstantiationStrategy;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanReference;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.exception.IocException;

public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory{

    private InstantiationStrategy instantiationStrategy = new SimpleInstantiation();

    @Override
    protected Object createBean(String beanName, BeanDefinition beanDefinition, Object[] args) throws IocException {
         Object bean;
         try {
            bean = createBeanInstance(beanDefinition, args);
            fillPropertyValues(beanDefinition, bean);
        } catch (Exception e) {
            throw new IocException("Initiation of bean failed", e);
        }
        addSingleton(beanName, bean);
         return bean;
    }

    private Object createBeanInstance(BeanDefinition beanDefinition, Object[] args) {
        Constructor<?> constructorToUse = null;
        Class<?> beanClass = beanDefinition.getBeanClass();
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
        PropertyValueList propertyValueList = beanDefinition.getPropertyValueList();
        if (propertyValueList == null) {
            return;
        }
        Class<?> beanClass = beanDefinition.getBeanClass();
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
                throw new IocException("Error setting property values: " + beanClass.getName(), e);
            }
        }

    }
    
    
    
}
