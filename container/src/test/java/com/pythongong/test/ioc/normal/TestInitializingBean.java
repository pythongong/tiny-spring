package com.pythongong.test.ioc.normal;

import com.pythongong.beans.config.InitializingBean;
import com.pythongong.stereotype.Component;

@Component("testInitializingBean")
public class TestInitializingBean implements InitializingBean {
    private boolean initialized = false;

    @Override
    public void afterPropertiesSet() {
        initialized = true;
    }

    public boolean isInitialized() {
        return initialized;
    }
}
