package com.eighttoten.auth.domain;

import static lombok.AccessLevel.PROTECTED;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = PROTECTED)
@RedisHash(value = "Auth", timeToLive = 604000)
public class Auth implements Serializable {
    @Id
    private String email;

    @Indexed
    private String refreshToken;

    public static Auth of(String memberEmail, String refreshToken) {
        Auth auth = new Auth();
        auth.email = memberEmail;
        auth.refreshToken = refreshToken;
        return auth;
    }
}