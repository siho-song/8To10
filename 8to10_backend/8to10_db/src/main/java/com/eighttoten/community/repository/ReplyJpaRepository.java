package com.eighttoten.community.repository;

import com.eighttoten.community.ReplyEntity;
import com.eighttoten.member.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyJpaRepository extends JpaRepository<ReplyEntity, Long> {
    List<ReplyEntity> findAllByMemberEntityId(Long memberId);
    List<ReplyEntity> findAllByPostEntityId(Long id);

    @Modifying
    @Query("delete from ReplyEntity r where r in :replies")
    void deleteByReplyIds(@Param(value = "replies") List<Long> replies);

    @EntityGraph(attributePaths = "memberEntity")
    @Query("select r from ReplyEntity r where r.id = :id")
    Optional<ReplyEntity> findByIdWithMember(@Param(value = "id") Long id);

    @EntityGraph(attributePaths = {"parent", "memberEntity"})
    @Query("select r from ReplyEntity r where r.id = :id")
    Optional<ReplyEntity> findByIdWithMemberAndParent(@Param(value = "id") Long id);

    @EntityGraph(attributePaths = {"memberEntity", "board"})
    @Query("select r from ReplyEntity  r where r.memberEntity.email = :email")
    List<ReplyEntity> findAllWithBoardAndMemberByEmail(@Param(value = "email") String email);

    @Query("select r from ReplyEntity r where r.parentId = :parentReplyId")
    List<ReplyEntity> findNestedRepliesByParentReplyId(@Param(value = "parentReplyId") Long parentReplyId);

    @EntityGraph(attributePaths = {"board"})
    @Query("select r from ReplyEntity r where r.memberEntity = :member")
    List<ReplyEntity> findAllByMemberWithBoard(@Param(value = "member") MemberEntity memberEntity);

}