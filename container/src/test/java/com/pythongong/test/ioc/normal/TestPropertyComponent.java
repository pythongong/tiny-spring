package com.pythongong.test.ioc.normal;

import com.pythongong.stereotype.Component;
import com.pythongong.stereotype.Value;

import lombok.Getter;

@Getter
@Component("testPropertyComponent")
public class TestPropertyComponent {
    @Value("${test.name}")
    private String name;

    @Value("${test.version}")
    private String version;

    @Value("${app.description}")
    private String description;

    @Value("${spring.datasource.auto-commit}")
    private boolean autoCommit;

}
