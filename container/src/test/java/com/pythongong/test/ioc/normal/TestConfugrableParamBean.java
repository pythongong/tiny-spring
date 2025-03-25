package com.pythongong.test.ioc.normal;

public class TestConfugrableParamBean {

    private TestBean testBean;

    public TestConfugrableParamBean(TestBean testBean) {
        this.testBean = testBean;
    }

    public boolean isProcessed() {
        return testBean != null;
    }
}
