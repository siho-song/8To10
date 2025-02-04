package com.eighttoten.community.domain.post;

import lombok.Getter;

@Getter
public class NewPostHeart {
    private Long memberId;
    private Long postId;

    public static NewPostHeart from(Long memberId, Long postId) {
        NewPostHeart newPostHeart = new NewPostHeart();
        newPostHeart.memberId = memberId;
        newPostHeart.postId = postId;
        return newPostHeart;
    }
}
