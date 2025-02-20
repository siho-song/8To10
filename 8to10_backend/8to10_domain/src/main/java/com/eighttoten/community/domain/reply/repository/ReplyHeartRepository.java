package com.eighttoten.community.domain.reply.repository;

import com.eighttoten.community.domain.reply.NewReplyHeart;
import java.util.List;

public interface ReplyHeartRepository {
    void save(NewReplyHeart newReplyHeart);
    void deleteAllByReplyId(Long replyId);
    void deleteAllByReplyIds(List<Long> replyIds);
    void deleteByMemberIdAndReplyId(Long memberId, Long replyId);
    boolean existsByMemberIdAndReplyId(Long memberId, Long replyId);
}