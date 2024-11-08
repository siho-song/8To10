package show.schedulemanagement.service.auth;

import static show.schedulemanagement.exception.ExceptionCode.INVALID_ACCESS_TOKEN;
import static show.schedulemanagement.exception.ExceptionCode.NOT_FOUND_REFRESH_TOKEN;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.auth.Auth;
import show.schedulemanagement.exception.InvalidTokenException;
import show.schedulemanagement.exception.NotFoundEntityException;
import show.schedulemanagement.provider.TokenProvider;
import show.schedulemanagement.repository.auth.AuthRepository;
import show.schedulemanagement.utils.BearerAuthorizationUtils;

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
