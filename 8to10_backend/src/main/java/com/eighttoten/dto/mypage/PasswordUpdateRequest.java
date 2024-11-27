package com.eighttoten.dto.mypage;

import com.eighttoten.validator.signup.fielderror.Password;
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