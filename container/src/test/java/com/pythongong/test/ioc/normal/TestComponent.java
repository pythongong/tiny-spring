package com.pythongong.test.ioc.normal;

import com.pythongong.stereotype.Component;

import lombok.Data;

@Data
@Component("testComponent")
public class TestComponent {

    private boolean postProcessed = false;
}