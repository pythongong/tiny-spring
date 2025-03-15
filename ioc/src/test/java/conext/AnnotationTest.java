package conext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.junit.jupiter.api.Test;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.context.annotation.PackageClassScanner;
import com.pythongong.context.annotation.ConfigurationClassParser;
import com.pythongong.context.support.PropertyResolver;
import util.com.test.TestApplication;

public class AnnotationTest {

    private static final String BASE_PACKAGE = "util.com.test";

    private static final String PATHA = "util.com.test.PathA";

    private static final String BEANA = "util.com.test.BeanA";

    private static final String BEANB = "util.com.test.inside.BeanB";
    
    @Test
    void test_Scan() throws ClassNotFoundException {
        PackageClassScanner scanner = new PackageClassScanner();
        Set<Class<?>> beanClasses = scanner.scan(BASE_PACKAGE);
        assertFalse(beanClasses.contains(Class.forName(PATHA)));
        assertTrue(beanClasses.contains(Class.forName(BEANA)));
        assertTrue(beanClasses.contains(Class.forName(BEANB)));
    }

    @Test
    void Test_Parse() throws ClassNotFoundException {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory(); 
        ConfigurationClassParser parser = new ConfigurationClassParser(new PropertyResolver(), beanFactory);
        Set<BeanDefinition> beanDefinitions = parser.parse(TestApplication.class);
        boolean isPathAExist = false;
        boolean isBeanAExist = false;
        boolean isBeanBExist = false;
        for (BeanDefinition beanDefinition : beanDefinitions) {
            if (!isPathAExist && beanDefinition.beanName().equals(PATHA)) {
                isPathAExist = true;
            }

            if (!isBeanAExist && beanDefinition.beanName().equals(BEANA)) {
                isBeanAExist = true;
            }

            if (!isBeanBExist && beanDefinition.beanName().equals(BEANB)) {
                isBeanBExist = true;
            }
        }
        assertFalse(isPathAExist);
        assertTrue(isBeanAExist);
        assertTrue(isBeanBExist);
        
    }
}
