package com.pythongong.test.utils;

import com.pythongong.stereotype.Component;
import com.pythongong.stereotype.PostConstruct;
import com.pythongong.stereotype.PreDestroy;

import lombok.Getter;

@Getter
@Component("lifecycleBean")
public class LifecycleTestBean {
    private boolean initialized = false;
    private boolean destroyed = false;

    @PostConstruct
    void init() {
        initialized = true;
    }

    @PreDestroy
    void destroy() {
        destroyed = true;
    }

}
