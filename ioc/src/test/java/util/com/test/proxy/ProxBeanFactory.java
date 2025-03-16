package util.com.test.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.pythongong.beans.FactoryBean;
import com.pythongong.stereotype.Component;
import com.pythongong.util.ClassUtils;

@Component("InforDao")
public class ProxBeanFactory implements FactoryBean<InforDao> {

    @Override
    public InforDao getObject() throws Exception {
        InvocationHandler handler = (proxy, method, args) -> {

            if ("toString".equals(method.getName())) {
                return this.toString();
            }
            
            Map<String, String> hashMap = new HashMap<>();
            hashMap.put("10001", "Tom");
            hashMap.put("10002", "Jim");
            hashMap.put("10003", "Frank");
            
            return "You're proxied " + method.getName() + " : " + hashMap.get(args[0].toString());
        };

        return (InforDao) Proxy.newProxyInstance(ClassUtils.getDefaultClassLoader(), new Class<?>[]{InforDao.class}, handler);
    }

}
