package com.pythongong.beans;

@FunctionalInterface
public interface FactoryBean<T> {

    T getObject() throws Exception;
}
