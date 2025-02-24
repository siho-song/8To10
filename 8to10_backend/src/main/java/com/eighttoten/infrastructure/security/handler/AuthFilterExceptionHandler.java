package com.eighttoten.infrastructure.security.handler;

import com.eighttoten.global.dto.ErrorResponse;
import com.eighttoten.infrastructure.security.exception.AuthException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthFilterExceptionHandler {

    private final int NO_CODE = 0;

    private final ObjectMapper objectMapper;

    public AuthFilterExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void handleException(HttpServletResponse response, AuthenticationException e) throws IOException {
        log.error(e.getMessage(), e);

        AuthException userAuthenticationException;
        ErrorResponse errorResponse;

        if(e instanceof AuthException){
            userAuthenticationException = (AuthException) e;
            errorResponse = ErrorResponse.of(userAuthenticationException.getCode(), e.getMessage());
        }
        else {
            errorResponse = ErrorResponse.of(NO_CODE, e.getMessage());
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");

        String error = objectMapper.writeValueAsString(errorResponse);

        PrintWriter writer = response.getWriter();

        writer.write(error);
        writer.flush();
        writer.close();
    }
}