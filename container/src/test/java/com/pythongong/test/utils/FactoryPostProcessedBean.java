package com.pythongong.test.utils;

import com.pythongong.stereotype.Component;

import lombok.Getter;

@Getter
@Component("beanFactoryPostProcessedBean")
public class FactoryPostProcessedBean {

    public final static String PROCESSED_NAME = "processed";

    private String name;

}
