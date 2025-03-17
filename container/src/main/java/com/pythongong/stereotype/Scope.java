package com.pythongong.stereotype;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.pythongong.enums.ScopeEnum;

@Retention(RetentionPolicy.RUNTIME)
@Target(value =  {ElementType.TYPE, ElementType.METHOD})
@Documented
public @interface Scope {

    ScopeEnum value() default ScopeEnum.SINGLETON;
    
}
