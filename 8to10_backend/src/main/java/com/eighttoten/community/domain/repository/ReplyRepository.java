package com.eighttoten.community.domain.repository;

import com.eighttoten.community.domain.Reply;
import com.eighttoten.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReplyRepository extends JpaRepository<Reply, Long> {
    @Modifying
    @Query("delete from Reply r where r in :replies")
    void deleteByReplies(@Param(value = "replies") List<Reply> replies);

    @EntityGraph(attributePaths = "parent")
    @Query("select r from Reply r where r.id = :id")
    Optional<Reply> findByIdWithParent(@Param(value = "id") Long id);

    @EntityGraph(attributePaths = {"parent", "member"})
    @Query("select r from Reply r where r.id = :id")
    Optional<Reply> findByIdWithMemberAndParent(@Param(value = "id") Long id);

    @EntityGraph(attributePaths = {"member", "board"})
    @Query("select r from Reply  r where r.member.email = :email")
    List<Reply> findAllWithBoardAndMemberByEmail(@Param(value = "email") String email);

    @Query("select r from Reply r where r.parent = :parent")
    List<Reply> findNestedRepliesByParent(@Param(value = "parent") Reply parent);

    @EntityGraph(attributePaths = {"board"})
    @Query("select r from Reply r where r.member = :member")
    List<Reply> findAllByMemberWithBoard(Member member);
}