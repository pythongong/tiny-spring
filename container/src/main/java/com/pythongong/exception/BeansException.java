package com.pythongong.exception;

public class BeansException extends RuntimeException{

    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable casuse) {
        super(msg, casuse);
    }
    
}
