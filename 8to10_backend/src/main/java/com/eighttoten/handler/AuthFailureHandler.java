package com.eighttoten.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class AuthFailureHandler implements AuthenticationFailureHandler {

    private final AuthFilterExceptionHandler authFilterExceptionHandler;

    public AuthFailureHandler(AuthFilterExceptionHandler authFilterExceptionHandler) {
        this.authFilterExceptionHandler = authFilterExceptionHandler;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        authFilterExceptionHandler.handleException(response, exception);
    }
}