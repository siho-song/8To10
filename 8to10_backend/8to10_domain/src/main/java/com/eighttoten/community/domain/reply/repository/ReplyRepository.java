package com.eighttoten.community.domain.reply.repository;

import com.eighttoten.community.domain.reply.NewReply;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.ReplyWithPost;
import java.util.List;
import java.util.Optional;

public interface ReplyRepository {
    void deleteById(Long id);
    void update(Reply reply);
    void deleteByReplyIds(List<Long> ids);
    long save(NewReply newReply);
    Optional<Reply> findById(Long id);
    Optional<ReplyWithPost> findByIdWithPost(Long id);
    List<Reply> findAllByPostId(Long id);
    List<Reply> findAllByMemberId(Long memberId);
    List<Reply> findNestedRepliesByParentId(Long parentReplyId);
}