package com.eighttoten.infrastructure.security.filter;

import com.eighttoten.global.exception.BadRequestException;
import com.eighttoten.global.exception.ExceptionCode;
import com.eighttoten.infrastructure.security.exception.AuthException;
import com.eighttoten.infrastructure.security.handler.AuthFilterExceptionHandler;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
public class EmailPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthFilterExceptionHandler authFilterExceptionHandler;

    public EmailPasswordAuthenticationFilter(AuthenticationManager authenticationManager,
                                             AuthFilterExceptionHandler authFilterExceptionHandler) {
        super(authenticationManager);
        this.authFilterExceptionHandler = authFilterExceptionHandler;
        setFilterProcessesUrl("/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        UsernamePasswordAuthenticationToken authRequest;
        try {
            authRequest = getAuthRequest(request);
            setDetails(request, authRequest);
        } catch (Exception e) {
            log.error("",e);
            throw new AuthException(ExceptionCode.USER_AUTHENTICATE_FAIL);
        }
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            super.doFilter(request, response, chain);
        } catch (AuthenticationException e) {
            authFilterExceptionHandler.handleException((HttpServletResponse) response, e);
        }
    }

    private UsernamePasswordAuthenticationToken getAuthRequest(HttpServletRequest request) {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        if(email == null || password == null){
            throw new BadRequestException(ExceptionCode.INVALID_REQUEST);
        }
        return new UsernamePasswordAuthenticationToken(email, password);
    }
}