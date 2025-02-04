package com.eighttoten.auth;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthRepositoryImpl implements AuthRepository {
    private final AuthRedisRepository authRedisRepository;

    @Override
    public Optional<Auth> findByRefreshToken(String refreshToken) {
        return authRedisRepository.findByRefreshToken(refreshToken).map(AuthEntity::toAuth);
    }

    @Override
    public void save(Auth auth) {
        authRedisRepository.save(AuthEntity.of(auth.getEmail(), auth.getRefreshToken()));
    }
}
