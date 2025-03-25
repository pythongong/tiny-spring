package com.pythongong.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.pythongong.beans.config.BeanPostProcessor;
import com.pythongong.context.ApplicationContext;
import com.pythongong.exception.BeansException;
import com.pythongong.exception.NoSuchBeanException;
import com.pythongong.util.ClassUtils;

public class AnnotationProxyBeanPostProcessor<A extends Annotation> implements BeanPostProcessor {

    private final Class<A> annotationClass;

    private final Map<String, Object> originBeanMap;

    private final ApplicationContext applicationContext;

    private final ProxyResolver proxyResolver;

    public AnnotationProxyBeanPostProcessor(ApplicationContext applicationContext) {
        annotationClass = getAnnotationClass();
        originBeanMap = new ConcurrentHashMap<>(ClassUtils.BIG_INITIAL_SIZE);
        this.applicationContext = applicationContext;
        proxyResolver = new ProxyResolver();
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        A annotation = beanClass.getAnnotation(annotationClass);
        if (annotation == null) {
            return bean;
        }

        try {
            String handlerName = (String) annotation.annotationType().getMethod("value").invoke(annotation);
            originBeanMap.put(beanName, bean);
            InvocationHandler handler = applicationContext.getBean(handlerName, InvocationHandler.class);
            if (handler == null) {
                throw new NoSuchBeanException(bean.getClass());
            }
            return proxyResolver.createProxy(bean, handler);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Object origin = this.originBeanMap.get(beanName);
        return origin != null ? origin : bean;
    }

    @SuppressWarnings("unchecked")
    private Class<A> getAnnotationClass() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            throw new IllegalArgumentException();
        }

        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length != 1) {
            throw new IllegalArgumentException();
        }

        Type annotationClass = actualTypeArguments[0];
        if (!(annotationClass instanceof Class<?>)) {
            throw new IllegalArgumentException();
        }

        return (Class<A>) annotationClass;
    }

}
