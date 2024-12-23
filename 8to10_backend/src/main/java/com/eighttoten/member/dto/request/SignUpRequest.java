package com.eighttoten.member.dto.request;

import com.eighttoten.member.domain.Gender;
import com.eighttoten.member.domain.Mode;
import com.eighttoten.member.validator.Nickname;
import com.eighttoten.member.validator.Password;
import com.eighttoten.member.validator.PhoneNumber;
import com.eighttoten.member.validator.Username;
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