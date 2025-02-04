package com.eighttoten.exception;

import lombok.Getter;

@Getter
public class AuthException extends RuntimeException{
    private final ExceptionCode exceptionCode;

    public AuthException(ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }
}
