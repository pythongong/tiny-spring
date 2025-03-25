package com.pythongong.test.ioc.normal;

import com.pythongong.beans.config.DisposableBean;
import com.pythongong.stereotype.Component;

@Component("testDisposableBean")
public class TestDisposableBean implements DisposableBean {
    private boolean destroyed = false;

    @Override
    public void destroy() {
        destroyed = true;
    }

    public boolean isDestroyed() {
        return destroyed;
    }
}
