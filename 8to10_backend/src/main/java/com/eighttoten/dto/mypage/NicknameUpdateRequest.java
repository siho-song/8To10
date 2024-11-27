package com.eighttoten.dto.mypage;

import com.eighttoten.validator.signup.fielderror.Nickname;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NicknameUpdateRequest {
    @Nickname
    @NotNull
    String nickname;
}