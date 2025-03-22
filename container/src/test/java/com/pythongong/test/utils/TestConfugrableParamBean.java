package com.pythongong.test.utils;

public class TestConfugrableParamBean {

    private TestBean testBean;

    public TestConfugrableParamBean(TestBean testBean) {
        this.testBean = testBean;
    }

    public boolean isProcessed() {
        return testBean != null;
    }
}
