package conext;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.context.annotation.ConfigurationClassParser;
import com.pythongong.context.support.AnnotationConfigApplicationContext;
import com.pythongong.context.support.PropertyResolver;

import util.com.test.TestApplication;
import util.com.test.init_destroy.Infor;
import util.com.test.inside.BeanB;
import util.com.test.post_process.MyBeanFactoryPostProcessor;
import util.com.test.post_process.MyBeanPostProcessor;

public class ApplicationContextTest {
    
    public static final String LOCATION = "New York";

    public static final String NAME = "Tom";

    @Test
    void test_InitAndDestroy() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestApplication.class);
        applicationContext.refresh();
        applicationContext.registerShutdownHook();
        Infor infor = (Infor) applicationContext.getBean("util.com.test.init_destroy.Infor");
        assertEquals("Tom", infor.getNameMap().get(1));
    }


    @Test
    void test_AnnotationConfigApplicationContext() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        ConfigurationClassParser parser = new ConfigurationClassParser(new PropertyResolver(), beanFactory);
        parser.parse(TestApplication.class);


        MyBeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(beanPostProcessor);
        

        BeanB beanB = (BeanB) beanFactory.getBean("util.com.test.inside.BeanB", "name", "location");

        assertEquals(NAME, beanB.getName());
        
        assertEquals(LOCATION, beanB.getLocation());
    }
}
