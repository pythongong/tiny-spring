package util.com.test.post_process;

import com.pythongong.beans.BeanFactoryPostProcessor;
import com.pythongong.beans.ConfigurableListableBeanFactory;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Component;

import conext.ApplicationContextTest;

@Component
public class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor{

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        BeanDefinition beanDefinition = beanFactory.getBeanDefinition("util.com.test.inside.BeanB");
        PropertyValueList propertyValueList = beanDefinition.propertyValueList();
        propertyValueList.addPropertyValue(new PropertyValue("name", ApplicationContextTest.NAME));
    }
    
}
