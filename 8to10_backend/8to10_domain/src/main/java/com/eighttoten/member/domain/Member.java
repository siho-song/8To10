package com.eighttoten.member.domain;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.MismatchException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
public class Member {
    private Long id;
    private String username;
    private String nickname;
    private String email;
    @Setter
    private String password;
    private String phoneNumber;
    private Gender gender;
    private Role role;
    private Mode mode;
    private String profileImagePath;
    private double score;
    private boolean authEmail;
    private boolean authPhone;

    public void checkIsSameEmail(String email) {
        if(!this.email.equals(email)) {
            throw new MismatchException(ExceptionCode.WRITER_NOT_EQUAL_MEMBER);
        }
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateProfilePhoto(String filePath) {
        this.profileImagePath = filePath;
    }
}
