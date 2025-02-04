package com.eighttoten.community.domain.reply;

import com.eighttoten.member.domain.Member;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReplyHeart {
    private Long id;
    private Reply reply;
    private Member member;
    private String createdBy;

    public static ReplyHeart from(Member member, Reply reply) {
        ReplyHeart replyHeart = new ReplyHeart();
        replyHeart.member = member;
        replyHeart.reply = reply;
        replyHeart.createdBy = null;
        return replyHeart;
    }
}
