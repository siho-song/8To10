package com.eighttoten.auth;

import static lombok.AccessLevel.PROTECTED;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@NoArgsConstructor(access = PROTECTED)
@RedisHash(value = "AuthEntity", timeToLive = 604000)
public class AuthEntity implements Serializable {
    @Id
    private String email;

    @Indexed
    private String refreshToken;

    public static AuthEntity of(String memberEmail, String refreshToken) {
        AuthEntity authEntity = new AuthEntity();
        authEntity.email = memberEmail;
        authEntity.refreshToken = refreshToken;
        return authEntity;
    }

    public Auth toAuth(){
        return new Auth(email, refreshToken);
    }
}