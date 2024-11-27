package com.eighttoten.exception;

public class InvalidTokenException extends AuthException {

    public InvalidTokenException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}
