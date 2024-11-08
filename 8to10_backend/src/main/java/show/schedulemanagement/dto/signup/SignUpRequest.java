package show.schedulemanagement.dto.signup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import show.schedulemanagement.domain.member.Gender;
import show.schedulemanagement.domain.member.Mode;
import show.schedulemanagement.validator.signup.fielderror.Nickname;
import show.schedulemanagement.validator.signup.fielderror.Password;
import show.schedulemanagement.validator.signup.fielderror.PhoneNumber;
import show.schedulemanagement.validator.signup.fielderror.Username;

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
    private Boolean authEmail;
    private Boolean authPhone;
}
