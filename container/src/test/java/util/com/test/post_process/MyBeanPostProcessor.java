package util.com.test.post_process;

import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Component;

import conext.ApplicationContextTest;
import util.com.test.inside.BeanB;

@Component
public class MyBeanPostProcessor implements BeanPostProcessor{

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        BeanB beanB = null;
        if (beanName.equals("util.com.test.inside.BeanB")) {
            beanB = (BeanB) bean;
            beanB.setLocation(ApplicationContextTest.LOCATION);;
        }
        return beanB != null ? beanB : bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
       return bean;
    }
    
}
