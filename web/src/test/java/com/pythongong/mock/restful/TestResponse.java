package com.pythongong.mock.restful;

import lombok.Getter;

@Getter
public class TestResponse {

    private final String name;

    public TestResponse(String name) {
        this.name = name;
    }

}
