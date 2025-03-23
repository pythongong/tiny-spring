package com.pythongong.test.utils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import com.pythongong.beans.config.FactoryBean;
import com.pythongong.stereotype.Component;

@Component("proxyBeanFactory")
public class ProxyBeanFactory implements FactoryBean<TestProxyBean> {

    public static final Map<Integer, String> data = new HashMap<>(3);

    static {
        data.put(10001, "Tom");
        data.put(10002, "Frank");
        data.put(10003, "Jack");
    }

    @Override
    public TestProxyBean getObject() throws Exception {
        InvocationHandler handler = (proxy, method, args) -> {
            return data.get(args[0]);
        };

        return (TestProxyBean) Proxy.newProxyInstance(
                ProxyBeanFactory.class.getClassLoader(),
                new Class[] { TestProxyBean.class },
                handler);
    }

    @Override
    public boolean isSingleton() {
        return true;
    }

}
