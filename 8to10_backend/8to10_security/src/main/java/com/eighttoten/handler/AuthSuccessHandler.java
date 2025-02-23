package com.eighttoten.handler;

import com.eighttoten.auth.Auth;
import com.eighttoten.auth.AuthRepository;
import com.eighttoten.support.BearerAuthorizationUtils;
import com.eighttoten.support.TokenProvider;
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
    private final AuthRepository authRepository;

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
        authRepository.save(Auth.of(email,refreshToken));

        response.setStatus(HttpStatus.OK.value());
    }

    private void setRefreshToken(HttpServletResponse response, String refreshToken) {
        Cookie jwtCookie = new Cookie("refresh_token", refreshToken);
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        response.addCookie(jwtCookie);
    }

    private void setAccessToken(HttpServletResponse response, String token){
        response.setHeader("Authorization", BearerAuthorizationUtils.BEARER_CODE + token);
    }
}