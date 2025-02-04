package com.eighttoten.exception;

public class InvalidTokenException extends CustomAuthenticationException {
    public InvalidTokenException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}