package show.schedulemanagement.dto.mypage;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import show.schedulemanagement.validator.signup.fielderror.Nickname;

@Getter
@NoArgsConstructor
public class NicknameUpdateRequest {
    @Nickname
    @NotNull
    String nickname;
}
