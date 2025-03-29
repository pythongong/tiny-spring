package com.pythongong.aop.proxy;

import com.pythongong.aop.AdvisedSupport;
import com.pythongong.util.CheckUtils;

public class ProxyFactory {

    public static Object createProxy(AdvisedSupport advisedSupport) {
        CheckUtils.nullArgs(advisedSupport, "ProxyFactory recevies null advise");
        // if (advisedSupport.isProxyTargetClass()) {
        // return new ByteBuddyAopProxy(advisedSupport).getProxy();
        // }
        return new JdkDynamicAopProxy(advisedSupport).getProxy();
    }
}
