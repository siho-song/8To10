package com.eighttoten.repository.auth;

import com.eighttoten.domain.auth.Auth;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByRefreshToken(String refreshToken);
}