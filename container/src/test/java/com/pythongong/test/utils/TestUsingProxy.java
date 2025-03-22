package com.pythongong.test.utils;

import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Component;

@Component("testUsingProxy")
public class TestUsingProxy {

    @AutoWired("proxyBeanFactory")
    private TestProxyBean testProxyBean;

    public String getName(Integer id) {
        return testProxyBean.queryUserName(id);
    }

}
