package util.com.test.aware;

import com.pythongong.beans.aware.BeanClassLoaderAware;
import com.pythongong.beans.aware.BeanFactoryAware;
import com.pythongong.beans.aware.BeanNameAware;
import com.pythongong.beans.factory.BeanFactory;
import com.pythongong.context.ApplicationContext;
import com.pythongong.context.ApplicationContextAware;
import com.pythongong.exception.BeansException;
import com.pythongong.stereotype.Component;

import lombok.Getter;

@Component("aware")
@Getter
public class AwareBean implements BeanFactoryAware, ApplicationContextAware, BeanNameAware, BeanClassLoaderAware {

    private ClassLoader classLoader;

    private String beanName;

    private ApplicationContext applicationContext;

    private BeanFactory beanFactory;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        
        this.beanFactory = beanFactory;
    }
    
}
