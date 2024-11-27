package com.eighttoten.service.board.reply;

import com.eighttoten.domain.board.reply.ReplyHeart;
import com.eighttoten.domain.member.Member;

public interface ReplyHeartService {
    boolean existsReplyHeartByMemberAndReplyId(Member member, Long replyId);
    ReplyHeart findByMemberAndReplyId(Member member, Long replyId);
    void add(Long replyId, Member member);
    void delete(Long replyId, Member member);
}
