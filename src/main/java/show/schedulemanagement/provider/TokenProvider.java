package show.schedulemanagement.provider;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class TokenProvider {
    private static final String ISSUER = "8to10_Server";

    private final Key key;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInSeconds)
    {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000;
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000;
    }

    public String generateAccessToken(String subject) {
        return  Jwts.builder()
                .setHeader(createHeader())
                .setSubject(subject)
                .setIssuer(ISSUER) //등록된 클레임
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(createExpirationDate(accessTokenValidityInMilliseconds))
                .compact();
    }

    public String generateRefreshToken(String subject) {
        return  Jwts.builder()
                .setHeader(createHeader())
                .setSubject(subject)
                .setIssuer(ISSUER) //등록된 클레임
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(createExpirationDate(refreshTokenValidityInMilliseconds))
                .compact();
    }

    public String getUserIdFromToken(String token) {
        String userId = parseToken(token)
                .getBody()
                .getSubject();

        if(userId == null){
            throw new UsernameNotFoundException("토큰에 유저 아이디가 존재하지 않습니다.");
        }

        return userId;
    }

    public boolean isValidToken(String token) {
        try {
            log.info("token : {} ", token);
            parseToken(token);
            return true;
        }
        catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isExpiredToken(String token) {
        try {
            parseToken(token);
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }

    private Jws<Claims> parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
    }

    private Map<String, Object> createHeader() {
        Map<String, Object> header = new HashMap<>();
        header.put(Header.TYPE, Header.JWT_TYPE);
        header.put("alg", "HS256");
        header.put("regDate", System.currentTimeMillis());
        return header;
    }

    private Date createExpirationDate(Long validityInMilliseconds) {
        Instant now = Instant.now();
        return Date.from(now.plusMillis(validityInMilliseconds));
    }
}