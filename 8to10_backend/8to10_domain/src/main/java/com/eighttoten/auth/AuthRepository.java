package com.eighttoten.auth;

import java.util.Optional;

public interface AuthRepository {
    Optional<Auth> findByRefreshToken(String refreshToken);
    void save(Auth auth);
}
