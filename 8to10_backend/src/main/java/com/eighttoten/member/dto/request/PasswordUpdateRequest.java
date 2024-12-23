package com.eighttoten.member.dto.request;

import com.eighttoten.member.validator.Password;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class PasswordUpdateRequest {
    @Password
    @NotNull
    String password;
}