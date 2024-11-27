package com.eighttoten.repository.board.reply;

import com.eighttoten.domain.board.reply.Reply;
import com.eighttoten.domain.board.reply.ReplyHeart;
import com.eighttoten.domain.member.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyHeartRepository extends JpaRepository<ReplyHeart, Long> {
    @Modifying
    @Query("delete from ReplyHeart rh where rh.reply in :replies")
    void deleteByReplies(@Param(value = "replies") List<Reply> replies);

    @Modifying
    @Query("delete from ReplyHeart rh where rh.reply.id = :replyId")
    void deleteByReplyId(@Param(value = "replyId") Long replyId);

    boolean existsReplyHeartByMemberAndReplyId(Member member, Long replyId);

    Optional<ReplyHeart> findByMemberAndReplyId(Member member, Long replyId);
}