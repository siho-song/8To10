package com.eighttoten.exception;

public class InvalidLevelException extends BusinessException {
    public InvalidLevelException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}