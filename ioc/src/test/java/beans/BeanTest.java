package beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pythongong.beans.Aware;
import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.beans.support.DefaultListableBeanFactory;

import util.com.test.aware.AwareBean;

public class BeanTest {

    private static final String AWARE_BEAN = "aware";

    @Test
    void test_Aware() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition(AWARE_BEAN, AwareBean.class);
        factory.registerBeanDefinition(beanDefinition);
        AwareBean awareBean = (AwareBean) factory.getBean(AWARE_BEAN);
        assertNotNull(awareBean.getBeanFactory());
        assertNotNull(awareBean.getBeanName());
        assertNotNull(awareBean.getClassLoader());
    }

    @Test
    void test_InitAndDestroy() throws NoSuchMethodException, SecurityException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition("userDao", UserDao.class, null, UserDao.class.getMethod("init"), UserDao.class.getMethod("destroy"));
        factory.registerBeanDefinition(beanDefinition);
        UserDao userDao = (UserDao) factory.getBean("userDao");
        assertEquals("Tom", userDao.getNameMap().get(1));
        factory.destroySingletons();
        assertTrue(userDao.getNameMap().isEmpty());
        
    }
    
    @Test
    void  test_Singleton() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition("userService",UserService.class);
        factory.registerBeanDefinition(beanDefinition);
        UserService userService1 = (UserService) factory.getBean("userService");
        UserService userService2 = (UserService) factory.getBean("userService");
        
        assertNotNull(userService1);
        assertNotNull(userService2);
        assertTrue(userService2 == userService1);

    }

    @Test
    void test_ArgsConstructor() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new BeanDefinition("userService",UserService.class);
        factory.registerBeanDefinition(beanDefinition);
        UserService userService = (UserService) factory.getBean("userService", "user");
        
        assertNotNull(userService);
    }

    @Test
    void test_PropertyList() {
        PropertyValueList propertyValueList = new PropertyValueList();
        propertyValueList.addPropertyValue(new PropertyValue("id", 100));
        for (PropertyValue propertyValue : propertyValueList) {
            assertEquals(propertyValue.name(), "id");
            assertEquals(propertyValue.value(), 100);
        }
    }

    @Test
    void test_PropertyFillForNonBean() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        PropertyValueList propertyValueList = new PropertyValueList();
        propertyValueList.addPropertyValue(new PropertyValue("id", 100));
        BeanDefinition beanDefinition = new BeanDefinition("userDao", UserDao.class,  propertyValueList, null, null);
        factory.registerBeanDefinition(beanDefinition);
        UserDao userDao = (UserDao) factory.getBean("userDao");

        assertEquals(userDao.getId(), 100);
    }

    @Test
    void test_PropertyFillForBean() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        
        PropertyValueList propertyValueList1 = new PropertyValueList();
        propertyValueList1.addPropertyValue(new PropertyValue("id", 100));
        BeanDefinition beanDefinition1 = new BeanDefinition("userDao", UserDao.class, propertyValueList1, null, null);
        factory.registerBeanDefinition(beanDefinition1);
        BeanDefinition beanDefinition2 = new BeanDefinition("userService",UserService.class);
        PropertyValueList propertyValueList2 = new PropertyValueList();
        propertyValueList2.addPropertyValue(new PropertyValue("name", "Alex"));
        propertyValueList2.addPropertyValue(new PropertyValue("userDao", (UserDao) factory.getBean("userDao")));
        factory.registerBeanDefinition(beanDefinition2);

        UserService userService = (UserService) factory.getBean("userService");
        UserDao userDao = new UserDao();
        userDao.setId(100);
        UserService userService2 = new UserService();
        userService2.setName("Alex");
        userService2.setUserDao(userDao);
        assertEquals(userService, userService);
    }
}
