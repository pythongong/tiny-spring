package com.pythongong.aop;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.pythongong.exception.AopConfigException;
import com.pythongong.util.CheckUtils;

import lombok.Builder;

@Builder
public record MethodInvocation(Object target, Method method, Object[] argus) {

    public MethodInvocation {
        CheckUtils.nullArgs(target, "MethodInvocation recevies null target");
        CheckUtils.nullArgs(method, "MethodInvocation recevies null method");
    }

    public Object proceed() {
        Object result = null;
        Class<?> returnType = method.getReturnType();
        if (!returnType.equals(void.class)) {
            try {
                result = method.invoke(target, argus);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new AopConfigException("method invoke in failure");
            }
        }
        return result;
    }

}
