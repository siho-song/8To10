package com.eighttoten.community.domain.post;

import lombok.Getter;

@Getter
public class NewPostScrap {
    private Long memberId;
    private Long postId;

    public static NewPostScrap from(Long memberId, Long postId) {
        NewPostScrap newPostScrap = new NewPostScrap();
        newPostScrap.memberId = memberId;
        newPostScrap.postId = postId;
        return newPostScrap;
    }
}
