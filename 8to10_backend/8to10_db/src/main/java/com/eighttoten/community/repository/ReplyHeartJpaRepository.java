package com.eighttoten.community.repository;

import com.eighttoten.community.ReplyHeartEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyHeartJpaRepository extends JpaRepository<ReplyHeartEntity, Long> {
    @Modifying
    @Query("delete from ReplyHeartEntity rh where rh.replyEntity.id = :replyId")
    void deleteAllByReplyId(@Param(value = "replyId") Long replyId);

    @Modifying
    @Query("delete from ReplyHeartEntity rh where rh.replyEntity.id in :replyIds")
    void deleteAllByReplyIds(@Param(value = "replyIds") List<Long> replyIds);

    void deleteByMemberEntityIdAndReplyEntityId(Long memberId, Long replyId);
    boolean existsByMemberEntityIdAndReplyEntityId(Long memberId, Long replyId);
}