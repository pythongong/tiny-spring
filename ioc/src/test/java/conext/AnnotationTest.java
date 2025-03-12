package conext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.pythongong.beans.BeanDefinitionRegistry;
import com.pythongong.beans.BeanFactory;
import com.pythongong.beans.impl.DefaultListableBeanFactory;
import com.pythongong.context.annotation.ClassPathBeanDefinitionScanner;
import com.pythongong.exception.IocException;

import util.com.test.BeanA;

public class AnnotationTest {
    
    @Test
    void test_Scan() {
        BeanDefinitionRegistry registry = new DefaultListableBeanFactory();  
        ClassPathBeanDefinitionScanner scanner = new ClassPathBeanDefinitionScanner(registry);
        scanner.scan("util.com.test");
        // get bean  
        BeanFactory beanFactory = (BeanFactory) registry;  
        assertThrows(IocException.class, () -> beanFactory.getBean("util.com.test.PathA"));
        assertNotNull((BeanA) beanFactory.getBean("util.com.test.BeanA") );
    }
}
