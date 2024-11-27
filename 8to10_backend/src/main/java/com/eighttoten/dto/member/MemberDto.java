package com.eighttoten.dto.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import com.eighttoten.domain.member.Gender;
import com.eighttoten.domain.member.Mode;

@Getter
@Setter
@Builder
@ToString
public class MemberDto {
    private Long id;
    private String email;
    private String nickname;
    private Gender gender;
    private Mode mode;
    private Double score;
}