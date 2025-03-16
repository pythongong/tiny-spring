package conext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.context.annotation.ConfigurableClassParser;
import com.pythongong.context.support.AnnotationConfigApplicationContext;
import com.pythongong.context.support.PropertyResolver;

import util.com.test.TestApplication;
import util.com.test.event.SignUpListener;
import util.com.test.event.SingUpEvent;
import util.com.test.init_destroy.Infor;
import util.com.test.inside.BeanB;
import util.com.test.post_process.MyBeanFactoryPostProcessor;
import util.com.test.post_process.MyBeanPostProcessor;
import util.com.test.proxy.InforDao;

public class ApplicationContextTest {
    
    public static final String LOCATION = "New York";

    public static final String NAME = "Tom";

    private static final String INFOR_DAO = "InforDao";

    private static final String SIGNUP_LISTENER = "SignUpListener";

    private static final String EVENT_MESSAGE = "success";

    @Test
    void test_Event() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestApplication.class);
        applicationContext.publishEvent(new SingUpEvent(applicationContext, EVENT_MESSAGE));
        SignUpListener listener = applicationContext.getBean(SIGNUP_LISTENER, SignUpListener.class);
        assertEquals(EVENT_MESSAGE, listener.getMessgae());
    }

    @Test
    void test_Proxy() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestApplication.class);
        InforDao inforDao = applicationContext.getBean(INFOR_DAO, InforDao.class);
        assertNotNull(inforDao);
    }


    @Test
    void test_InitAndDestroy() {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(TestApplication.class);
        
        applicationContext.registerShutdownHook();
        Infor infor = (Infor) applicationContext.getBean("util.com.test.init_destroy.Infor");
        assertEquals("Tom", infor.getNameMap().get(1));
    }


    @Test
    void test_AnnotationConfigApplicationContext() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();

        ConfigurableClassParser parser = new ConfigurableClassParser(new PropertyResolver());
        parser.parse(TestApplication.class);


        MyBeanFactoryPostProcessor beanFactoryPostProcessor = new MyBeanFactoryPostProcessor();
        beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);

        MyBeanPostProcessor beanPostProcessor = new MyBeanPostProcessor();
        beanFactory.addBeanPostProcessor(beanPostProcessor);
        

        BeanB beanB = (BeanB) beanFactory.getBean("util.com.test.inside.BeanB");

        assertEquals(NAME, beanB.getName());
        
        assertEquals(LOCATION, beanB.getLocation());
    }
}
