package com.eighttoten.global.exception;

public class BadRequestException extends BusinessException {
    public BadRequestException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}