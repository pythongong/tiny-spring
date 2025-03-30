package com.pythongong.aop.aspectj;

import java.lang.reflect.Method;

public interface MethodMatcher {
    boolean matches(Method method);
}
