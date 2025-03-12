package com.pythongong.exception;

public class IocException extends RuntimeException{

    public IocException(String msg) {
        super(msg);
    }

    public IocException(String msg, Throwable casuse) {
        super(msg, casuse);
    }
    
}
