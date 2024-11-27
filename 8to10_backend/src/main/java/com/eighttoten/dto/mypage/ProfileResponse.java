package com.eighttoten.dto.mypage;

import com.eighttoten.domain.member.Member;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ProfileResponse {
    private String nickname;
    private String email;
    private String role;
    private String profileImageUrl;

    public static ProfileResponse from(Member member){
        ProfileResponse response = new ProfileResponse();
        response.email = member.getEmail();
        response.nickname = member.getNickname();
        response.role = member.getRole().getValue();
        response.profileImageUrl = member.getImageFile();
        return response;
    }
}