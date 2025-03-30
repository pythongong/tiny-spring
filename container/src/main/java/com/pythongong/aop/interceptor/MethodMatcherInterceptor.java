package com.pythongong.aop.interceptor;

import com.pythongong.aop.aspectj.MethodMatcher;

public record MethodMatcherInterceptor(MethodInterceptor methodInterceptor, MethodMatcher methodMatcher) {

}
