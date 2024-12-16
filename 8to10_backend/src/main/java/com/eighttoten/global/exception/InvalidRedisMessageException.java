package com.eighttoten.global.exception;

public class InvalidRedisMessageException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    public InvalidRedisMessageException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.exceptionCode = exceptionCode;
    }
}