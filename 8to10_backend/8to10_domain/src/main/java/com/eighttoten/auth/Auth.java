package com.eighttoten.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Auth {
    private String email;
    private String refreshToken;

    public static Auth of(String email, String refreshToken) {
        return new Auth(email, refreshToken);
    }
}
