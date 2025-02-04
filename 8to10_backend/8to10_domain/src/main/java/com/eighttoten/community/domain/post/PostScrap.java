package com.eighttoten.community.domain.post;

import com.eighttoten.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class PostScrap {
    private Long id;
    private Post post;
    private Member member;
    private String createdBy;
}
