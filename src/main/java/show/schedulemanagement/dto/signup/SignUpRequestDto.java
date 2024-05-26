package show.schedulemanagement.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import show.schedulemanagement.domain.member.Gender;
import show.schedulemanagement.domain.member.Mode;
import show.schedulemanagement.validator.signup.Nickname;
import show.schedulemanagement.validator.signup.Password;
import show.schedulemanagement.validator.signup.PhoneNumber;
import show.schedulemanagement.validator.signup.Username;

@Getter
@Setter
@Builder
public class SignUpRequestDto {
    @NotBlank
    @Username
    private String username;

    @NotBlank
    @Nickname
    private String nickname;

    @Email
    private String email;

    @Password
    private String password;

    @PhoneNumber
    @NotBlank
    private String phoneNumber;

    private Gender gender;
    private Mode mode;
    private Boolean authEmail;
    private Boolean authPhone;
}
