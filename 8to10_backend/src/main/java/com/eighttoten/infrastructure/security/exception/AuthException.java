package com.eighttoten.infrastructure.security.exception;

import com.eighttoten.global.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthException extends AuthenticationException {
    private final int code;
    private final String message;

    public AuthException(ExceptionCode exceptionCode) {
        super(exceptionCode.getMessage());
        this.code = exceptionCode.getCode();
        this.message = exceptionCode.getMessage();
    }
}