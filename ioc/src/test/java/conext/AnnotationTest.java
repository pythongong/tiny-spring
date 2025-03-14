package conext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.BeanFactory;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.context.annotation.ClassPathBeanDefinitionScanner;
import com.pythongong.context.annotation.ConfigurationClassParser;
import com.pythongong.exception.BeansException;

import util.com.test.BeanA;
import util.com.test.TestApplication;
import util.com.test.inside.BeanB;

public class AnnotationTest {

    
    @Test
    void test_Scan() {
        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();  
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.scan("util.com.test");
        // get bean  
        BeanFactory beanFactory = (BeanFactory) registry;  
        assertThrows(BeansException.class, () -> beanFactory.getBean("util.com.test.PathA"));
        assertNotNull((BeanA) beanFactory.getBean("util.com.test.BeanA") );
    }

    @Test
    void Test_Parse() {
        BeanDefinitionRegistry registry = new DefaultListableBeanFactory(); 
        ConfigurationClassParser parser = new ConfigurationClassParser(registry);
        parser.parse(TestApplication.class);
        // get bean  
        BeanFactory beanFactory = (BeanFactory) registry;  
        assertThrows(BeansException.class, () -> beanFactory.getBean("util.com.test.PathA"));
        assertNotNull((BeanB) beanFactory.getBean("util.com.test.inside.BeanB") );
        assertNotNull((BeanA) beanFactory.getBean("util.com.test.BeanA") );
    }
}
