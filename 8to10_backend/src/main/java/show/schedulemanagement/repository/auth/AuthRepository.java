package show.schedulemanagement.repository.auth;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import show.schedulemanagement.domain.auth.Auth;

public interface AuthRepository extends JpaRepository<Auth, Long> {
    Optional<Auth> findByRefreshToken(String refreshToken);
}
