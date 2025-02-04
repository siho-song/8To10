package com.eighttoten.member.dto.request;

import com.eighttoten.member.validator.Nickname;
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