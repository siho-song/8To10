package com.eighttoten.service;

import static com.eighttoten.exception.ExceptionCode.INVALID_ACCESS_TOKEN;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_REFRESH_TOKEN;

import com.eighttoten.auth.Auth;
import com.eighttoten.auth.AuthRepository;
import com.eighttoten.exception.AuthException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.support.BearerAuthorizationUtils;
import com.eighttoten.support.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final TokenProvider tokenProvider;
    private final BearerAuthorizationUtils bearerUtils;

    public String getRenewalAccessToken(String refreshToken, String accessTokenHeader){
        String accessToken = bearerUtils.extractToken(accessTokenHeader);

        tokenProvider.validateRefreshToken(refreshToken);

        if(tokenProvider.isValidToken(accessToken)){
            return accessToken;
        }

        if(tokenProvider.isExpiredToken(accessToken)){
            Auth auth = authRepository.findByRefreshToken(refreshToken)
                    .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REFRESH_TOKEN));

            String email = auth.getEmail();
            return tokenProvider.generateAccessToken(email);
        }
        throw new AuthException(INVALID_ACCESS_TOKEN);
    }
}