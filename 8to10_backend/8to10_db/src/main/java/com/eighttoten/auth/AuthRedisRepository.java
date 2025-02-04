package com.eighttoten.auth;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AuthRedisRepository extends CrudRepository<AuthEntity, String> {
    Optional<AuthEntity> findByRefreshToken(String refreshToken);
}