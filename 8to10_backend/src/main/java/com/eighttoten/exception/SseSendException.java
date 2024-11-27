package com.eighttoten.exception;

public class SseSendException extends RuntimeException{
    private final ExceptionCode exceptionCode;


    public SseSendException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}
