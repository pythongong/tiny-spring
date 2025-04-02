package com.pythongong.web;

import java.lang.reflect.Method;

import lombok.Builder;

@Builder
public record Dispatcher(boolean isPost, Object controller, Method method, String url) {

}
