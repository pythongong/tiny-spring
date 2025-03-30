package com.pythongong.test.aop.valid;

import com.pythongong.stereotype.Component;

@Component
public class AopTestTarget implements AopTestInterface {

    private boolean isProxy = false;

    @Override
    public boolean getProxy() {
        return isProxy;
    }

    @Override
    public int add(int i, int j) {
        return i + j;
    }

}
