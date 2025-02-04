package com.eighttoten.community.domain.reply;

import lombok.Getter;

@Getter
public class NewReplyHeart {
    Long memberId;
    Long replyId;

    public static NewReplyHeart from(Long memberId, Long replyId) {
        NewReplyHeart newReplyHeart = new NewReplyHeart();
        newReplyHeart.memberId= memberId;
        newReplyHeart.replyId = replyId;
        return newReplyHeart;
    }
}
