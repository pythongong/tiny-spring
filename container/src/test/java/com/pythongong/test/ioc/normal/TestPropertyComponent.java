package com.pythongong.test.ioc.normal;

import com.pythongong.stereotype.Component;
import com.pythongong.stereotype.Value;

@Component("testPropertyComponent")
public class TestPropertyComponent {
    @Value("${test.name}")
    private String name;

    @Value("${test.version}")
    private String version;

    @Value("${app.description}")
    private String description;

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public String getDescription() {
        return description;
    }
}
