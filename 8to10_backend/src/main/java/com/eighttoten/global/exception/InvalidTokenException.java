package com.eighttoten.global.exception;

import com.eighttoten.infrastructure.security.exception.AuthException;

public class InvalidTokenException extends AuthException {
    public InvalidTokenException(ExceptionCode exceptionCode) {
        super(exceptionCode);
    }
}