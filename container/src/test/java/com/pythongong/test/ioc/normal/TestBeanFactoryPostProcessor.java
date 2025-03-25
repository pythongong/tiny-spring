package com.pythongong.test.ioc.normal;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.BeanFactoryPostProcessor;
import com.pythongong.beans.config.FieldValue;
import com.pythongong.beans.factory.ConfigurableListableBeanFactory;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Component;

@Component
public class TestBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("beanFactoryPostProcessedBean");
        if (beanDefinition == null) {
            return;
        }
        beanDefinition.fieldValueList().add(new FieldValue("name", FactoryPostProcessedBean.PROCESSED_NAME));
    }

}
