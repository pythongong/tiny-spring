package com.pythongong.test.utils;

import com.pythongong.stereotype.AutoWired;

public class BeanWithMethodInjection {
    private TestBean testBean;

    @AutoWired
    public void setTestBean(TestBean testBean) {
        this.testBean = testBean;
    }

    public TestBean getTestBean() {
        return testBean;
    }
}
