package com.eighttoten.repository.auth;

import com.eighttoten.domain.auth.Auth;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

public interface AuthRedisRepository extends CrudRepository<Auth, String> {
    Optional<Auth> findByRefreshToken(String refreshToken);
}