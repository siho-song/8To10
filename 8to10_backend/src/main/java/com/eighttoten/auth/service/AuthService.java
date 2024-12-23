package com.eighttoten.auth.service;

import static com.eighttoten.global.exception.ExceptionCode.INVALID_ACCESS_TOKEN;
import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_REFRESH_TOKEN;

import com.eighttoten.auth.domain.Auth;
import com.eighttoten.auth.domain.AuthRedisRepository;
import com.eighttoten.global.exception.InvalidTokenException;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.global.utils.BearerAuthorizationUtils;
import com.eighttoten.infrastructure.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRedisRepository authRedisRepository;
    private final TokenProvider tokenProvider;
    private final BearerAuthorizationUtils bearerUtils;

    public Auth findByRefreshToken(String refreshToken) {
        return authRedisRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REFRESH_TOKEN));
    }

    public void save(String email, String refreshToken) {
        authRedisRepository.save(Auth.of(email,refreshToken));
    }

    public String getRenewalAccessToken(String refreshToken, String accessTokenHeader){
        String accessToken = bearerUtils.extractToken(accessTokenHeader);

        tokenProvider.validateRefreshToken(refreshToken);

        if(tokenProvider.isValidToken(accessToken)){
            return accessToken;
        }

        if(tokenProvider.isExpiredToken(accessToken)){
            Auth auth = findByRefreshToken(refreshToken);
            String email = auth.getEmail();
            return tokenProvider.generateAccessToken(email);
        }
        throw new InvalidTokenException(INVALID_ACCESS_TOKEN);
    }
}