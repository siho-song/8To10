package com.eighttoten.exception;

public class InvalidScheduleCreationException extends BusinessException {
    public InvalidScheduleCreationException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}