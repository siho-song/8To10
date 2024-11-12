package show.schedulemanagement.dto.mypage;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import show.schedulemanagement.validator.signup.fielderror.Password;

@NoArgsConstructor
@Getter
public class PasswordUpdateRequest {
    @Password
    @NotNull
    String password;
}
