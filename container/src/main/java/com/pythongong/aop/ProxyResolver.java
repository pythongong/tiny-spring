package com.pythongong.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.pythongong.exception.BeansException;
import com.pythongong.util.CheckUtils;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.scaffold.subclass.ConstructorStrategy;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;

public class ProxyResolver {
    private final ByteBuddy byteBuddy;

    public ProxyResolver() {
        this.byteBuddy = new ByteBuddy();
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxy(T bean, InvocationHandler invocationHandler) {
        CheckUtils.nullArgs(bean, "ProxyResolver.createProxy recevies null bean");
        CheckUtils.nullArgs(invocationHandler, "ProxyResolver.createProxy recevies null invocationHandler");

        Class<?> beanClass = bean.getClass();

        Class<?> proxyClass = byteBuddy
                .subclass(beanClass, ConstructorStrategy.Default.DEFAULT_CONSTRUCTOR)
                .method(ElementMatchers.isPublic())
                .intercept(InvocationHandlerAdapter.of(
                        new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                                return invocationHandler.invoke(proxy, method, args);
                            }

                        }))
                .make().load(beanClass.getClassLoader())
                .getLoaded();

        try {
            return (T) proxyClass.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
                | NoSuchMethodException | SecurityException e) {
            throw new BeansException(String.format("Crete proxy bean for {%s} failed", beanClass.getName()));
        }
    }

}
