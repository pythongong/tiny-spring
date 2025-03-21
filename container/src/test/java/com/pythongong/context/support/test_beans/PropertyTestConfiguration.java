package com.pythongong.context.support.test_beans;

import com.pythongong.stereotype.ComponentScan;
import com.pythongong.stereotype.Configuration;

@Configuration
@ComponentScan(basePackages = "com.pythongong.context.support")
public class PropertyTestConfiguration {
    public PropertyTestConfiguration() {}
}