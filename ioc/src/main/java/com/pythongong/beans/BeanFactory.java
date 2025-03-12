package com.pythongong.beans;

import com.pythongong.exception.IocException;

public interface BeanFactory {

    Object getBean(String name) throws IocException;

    Object getBean(String name, Object... args) throws IocException;
} 