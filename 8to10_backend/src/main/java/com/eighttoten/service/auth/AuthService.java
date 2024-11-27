package com.eighttoten.service.auth;

import static com.eighttoten.exception.ExceptionCode.INVALID_ACCESS_TOKEN;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_REFRESH_TOKEN;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.domain.auth.Auth;
import com.eighttoten.exception.InvalidTokenException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.provider.TokenProvider;
import com.eighttoten.repository.auth.AuthRepository;
import com.eighttoten.utils.BearerAuthorizationUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthRepository authRepository;
    private final TokenProvider tokenProvider;
    private final BearerAuthorizationUtils bearerUtils;

    @Transactional
    public void save(String email, String refreshToken) {
        Auth auth = Auth.from(email, refreshToken);
        authRepository.save(auth);
    }

    @Transactional(readOnly = true)
    public Auth findByRefreshToken(String refreshToken) {
        return authRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REFRESH_TOKEN));
    }

    @Transactional(readOnly = true)
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
