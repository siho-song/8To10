package show.schedulemanagement.service.auth;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.auth.Auth;
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
                .orElseThrow(() -> new EntityNotFoundException("해당 리프레시 토큰이 존재하지 않습니다."));
    }

    @Transactional(readOnly = true)
    public String getRenewalAccessToken(String refreshToken, String accessTokenHeader){
        String accessToken = bearerUtils.extractToken(accessTokenHeader);

        if(tokenProvider.isValidToken(refreshToken)){
            if(tokenProvider.isExpiredToken(accessToken)){
                Auth auth = findByRefreshToken(refreshToken);
                String email = auth.getEmail();
                return tokenProvider.generateAccessToken(email);
            }
            if(tokenProvider.isValidToken(accessToken)){
                return accessToken;
            }
        }
        throw new JwtException("리프레시 토큰이 유효하지 않습니다.");
    }
}
