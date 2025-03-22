package com.pythongong.test.utils;

import com.pythongong.stereotype.AutoWired;
import com.pythongong.stereotype.Component;

@Component("testComponent")
public class TestConstructorInjection {

    private TestComponent testComponent;

    @AutoWired
    public TestConstructorInjection(TestComponent testComponent) {
        this.testComponent = testComponent;
    }

    public boolean isProcessed() {
        return testComponent.isPostProcessed();
    }
}