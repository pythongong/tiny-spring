package beans;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.pythongong.beans.config.BeanDefinition;
import com.pythongong.beans.config.PropertyValue;
import com.pythongong.beans.config.PropertyValueList;
import com.pythongong.beans.support.DefaultListableBeanFactory;
import com.pythongong.enums.ScopeEnum;

import util.com.test.aware.AwareBean;

public class BeanTest {

    private static final String AWARE_BEAN = "aware";

    private static final String USER_SERVICE = "userSerive";

    private static final String USER_DAO = "userDao";

    @Test
    void test_Prototype() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = BeanDefinition.builder()
        .scope(ScopeEnum.PROTOTYPE)
        .beanClass( AwareBean.class)
        .beanName(AWARE_BEAN)
        .build();
        
        factory.registerBeanDefinition(beanDefinition);
        AwareBean awareBean1 = factory.getBean(AWARE_BEAN, AwareBean.class);
        AwareBean awareBean2 = factory.getBean(AWARE_BEAN, AwareBean.class);
        assertFalse(awareBean1 == awareBean2);
    }

    @Test
    void test_Aware() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = BeanDefinition.builder()
        .beanClass( AwareBean.class)
        .beanName(AWARE_BEAN)
        .build();
        
        factory.registerBeanDefinition(beanDefinition);
        AwareBean awareBean = (AwareBean) factory.getBean(AWARE_BEAN);
        assertNotNull(awareBean.getBeanFactory());
        assertNotNull(awareBean.getBeanName());
        assertNotNull(awareBean.getClassLoader());
    }

    @Test
    void test_InitAndDestroy() throws NoSuchMethodException, SecurityException {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = BeanDefinition.builder()
        .beanClass(UserDao.class)
        .beanName(USER_DAO)
        .initMethod(UserDao.class.getMethod("init"))
        .destroyMethod(UserDao.class.getMethod("destroy"))
        .build();
        factory.registerBeanDefinition(beanDefinition);
        UserDao userDao = (UserDao) factory.getBean("userDao");
        assertEquals("Tom", userDao.getNameMap().get(1));
        factory.destroySingletons();
        assertTrue(userDao.getNameMap().isEmpty());
        
    }
    
    @Test
    void  test_Singleton() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = BeanDefinition.builder()
        .beanClass(UserService.class)
        .beanName(USER_SERVICE)
        .build();
        factory.registerBeanDefinition(beanDefinition);
        UserService userService1 = (UserService) factory.getBean("userService");
        UserService userService2 = (UserService) factory.getBean("userService");
        
        assertNotNull(userService1);
        assertNotNull(userService2);
        assertTrue(userService2 == userService1);

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
        BeanDefinition beanDefinition = BeanDefinition.builder()
        .beanClass(UserDao.class)
        .beanName(USER_DAO)
        .build();
        factory.registerBeanDefinition(beanDefinition);
        UserDao userDao = (UserDao) factory.getBean("userDao");

        assertEquals(userDao.getId(), 100);
    }

    @Test
    void test_PropertyFillForBean() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        
        PropertyValueList propertyValueList1 = new PropertyValueList();
        propertyValueList1.addPropertyValue(new PropertyValue("id", 100));
        BeanDefinition beanDefinition1 = BeanDefinition.builder()
        .beanClass(UserDao.class)
        .beanName(USER_DAO)
        .build();
        factory.registerBeanDefinition(beanDefinition1);
        BeanDefinition beanDefinition2 = BeanDefinition.builder()
        .beanClass(UserService.class)
        .beanName(USER_SERVICE)
        .build();;
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
