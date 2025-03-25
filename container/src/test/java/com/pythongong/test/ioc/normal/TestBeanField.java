package com.pythongong.test.ioc.normal;

import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Component;

@Component("testBeanField")
public class TestBeanField {
    @AutoWired
    private TestBean testBean;

    @AutoWired("testComponent")
    private TestComponent testComponent;
}
