package com.pythongong.util;

@FunctionalInterface
public interface StringConverter {

    Object convert(String source);

}