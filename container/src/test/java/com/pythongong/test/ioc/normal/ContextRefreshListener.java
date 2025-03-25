
package com.pythongong.test.ioc.normal;

import com.pythongong.context.event.ApplicationListener;
import com.pythongong.context.event.ContextRefreshedEvent;
import com.pythongong.stereotype.Component;

import lombok.Getter;

@Getter
@Component("contextRefreshListener")
public class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    private boolean refreshed = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        refreshed = true;
    }

}