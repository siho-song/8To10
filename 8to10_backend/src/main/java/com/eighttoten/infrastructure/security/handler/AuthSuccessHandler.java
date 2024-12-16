package com.eighttoten.infrastructure.security.handler;

import static com.eighttoten.global.utils.BearerAuthorizationUtils.BEARER_CODE;

import com.eighttoten.auth.service.AuthService;
import com.eighttoten.infrastructure.TokenProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

    private static final String EMPTY_SUBJECT="";

    private final TokenProvider tokenProvider;
    private final AuthService authService;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
    {
        String email = authentication.getName();

        String accessToken = tokenProvider.generateAccessToken(email);
        setAccessToken(response, accessToken);

        String refreshToken = tokenProvider.generateRefreshToken(EMPTY_SUBJECT);
        setRefreshToken(response, refreshToken);
        authService.save(email, refreshToken);

        response.setStatus(HttpStatus.OK.value());
    }

    private void setRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie jwtCookie = new Cookie("refresh_token", refreshToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
    }

    private void setAccessToken(HttpServletResponse response, String token){
        response.setHeader("Authorization", BEARER_CODE + token);
    }
}