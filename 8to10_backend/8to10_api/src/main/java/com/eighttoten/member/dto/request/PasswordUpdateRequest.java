package com.eighttoten.member.dto.request;

import com.eighttoten.member.validator.Password;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordUpdateRequest {
    @Password
    @NotNull
    String password;
}