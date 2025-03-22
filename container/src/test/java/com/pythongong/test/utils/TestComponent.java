package com.pythongong.test.utils;

import com.pythongong.stereotype.Component;

import lombok.Data;

@Data
@Component("testComponent")
public class TestComponent {
    
    private boolean postProcessed = false;
}