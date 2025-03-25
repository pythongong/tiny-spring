package com.pythongong.test.ioc.normal;

import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Component;

@Component("beanWithConstructorInjection")
public class BeanWithConstructorInjection {
    private TestBean testBean;

    @AutoWired
    public BeanWithConstructorInjection(TestBean testBean) {
        this.testBean = testBean;
    }

    public TestBean getTestBean() {
        return testBean;
    }
}