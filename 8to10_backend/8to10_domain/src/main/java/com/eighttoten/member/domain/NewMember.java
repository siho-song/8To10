package com.eighttoten.member.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class NewMember {
    private String username;
    private String nickname;
    private String email;
    @Setter
    private String password;
    private String phoneNumber;
    private Gender gender;
    private Role role;
    private Mode mode;
    private boolean isAuthEmail;
    private boolean isAuthPhone;
}
