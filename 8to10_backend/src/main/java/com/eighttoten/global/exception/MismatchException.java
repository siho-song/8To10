package com.eighttoten.global.exception;

public class MismatchException extends BusinessException {
    public MismatchException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}