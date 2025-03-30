package com.pythongong.aop.proxy;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.util.CheckUtils;

public class ProxyFactory {

    public static Object createProxy(AdvisedSupport advisedSupport) {
        CheckUtils.nullArgs(advisedSupport, "ProxyFactory recevies null advise");
        Object target = advisedSupport.target();
        Class<?> targetClass = target.getClass();
        if (targetClass.isInterface()) {
            return new JdkDynamicAopProxy(advisedSupport).getProxy();
        }
        return new ByteBuddyAopProxy(advisedSupport).getProxy();
    }
}
