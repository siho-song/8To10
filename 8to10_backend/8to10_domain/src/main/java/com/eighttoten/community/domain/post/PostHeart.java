package com.eighttoten.community.domain.post;

import com.eighttoten.member.domain.Member;
import lombok.Getter;

@Getter
public class PostHeart {
    private Long id;
    private Member member;
    private Post post;
    private String createdBy;
}
