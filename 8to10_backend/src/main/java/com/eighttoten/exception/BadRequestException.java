package com.eighttoten.exception;

public class BadRequestException extends BusinessException {

    public BadRequestException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
