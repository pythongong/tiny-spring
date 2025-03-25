package com.pythongong.test.ioc.normal;

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
    public void init() {
        initialized = true;
    }

    @PreDestroy
    public void destroy() {
        destroyed = true;
    }

}
