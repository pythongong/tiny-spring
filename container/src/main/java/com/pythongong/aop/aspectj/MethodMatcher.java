package com.pythongong.aop.aspectj;

import java.lang.reflect.Method;

@FunctionalInterface
public interface MethodMatcher {
    boolean matches(Method method);
}
