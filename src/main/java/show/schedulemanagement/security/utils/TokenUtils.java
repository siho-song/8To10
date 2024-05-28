package show.schedulemanagement.security.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import show.schedulemanagement.security.dto.LoginMemberDto;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TokenUtils {

    private final Key key;
    private final long tokenValidityInMilliseconds;

    public TokenUtils(@Value("${jwt.secret}") String secret, @Value("${jwt.access-token-validity-in-seconds}") long tokenValidityInSeconds) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
    }

    public String generateJwtToken(LoginMemberDto loginMemberDto) {
        String token = Jwts.builder()
                .setHeader(createHeader())
                .setClaims(createClaims(loginMemberDto))
                .setSubject(loginMemberDto.getEmail())
                .setIssuer("schedulemanagement")
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(createExpirationDate())
                .compact();
        log.debug("Generated Token: {}", token);
        return token;
    }

    public boolean isValidToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            log.error("Invalid JWT token", e);
            return false;
        }
    }

    public String getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private Map<String, Object> createClaims(LoginMemberDto loginMemberDto) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("loginId", loginMemberDto.getEmail());
        return claims;
    }

    private Date createExpirationDate() {
        Instant now = Instant.now();
        Instant expiryDate = now.plus(Duration.ofMillis(tokenValidityInMilliseconds));
        return Date.from(expiryDate);
    }
}