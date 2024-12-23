package com.eighttoten.global.exception;

public class NotFoundEntityException extends BusinessException {
    public NotFoundEntityException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}