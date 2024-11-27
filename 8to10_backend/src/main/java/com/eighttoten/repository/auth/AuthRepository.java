package com.eighttoten.repository.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.eighttoten.domain.auth.Auth;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByRefreshToken(String refreshToken);
}
