package com.eighttoten.dto.signup;

import com.eighttoten.domain.member.Gender;
import com.eighttoten.domain.member.Mode;
import com.eighttoten.validator.signup.fielderror.Nickname;
import com.eighttoten.validator.signup.fielderror.Password;
import com.eighttoten.validator.signup.fielderror.PhoneNumber;
import com.eighttoten.validator.signup.fielderror.Username;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SignUpRequest {
    @NotBlank
    @Username
    private String username;

    @NotBlank
    @Nickname
    private String nickname;

    @Email
    @NotBlank
    private String email;

    @Password
    @NotBlank
    private String password;

    @PhoneNumber
    @NotBlank
    private String phoneNumber;

    private Gender gender;
    private Mode mode;
    private boolean isAuthEmail;
    private boolean isAuthPhone;
}