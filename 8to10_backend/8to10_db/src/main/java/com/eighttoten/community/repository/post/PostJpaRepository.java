package com.eighttoten.community.repository.post;

import com.eighttoten.community.PostEntity;
import com.eighttoten.member.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long>, PostRepositoryCustom {
    @EntityGraph(attributePaths = {"memberEntity"})
    @Query("select p from PostEntity p where p.id = :id")
    Optional<PostEntity> findByIdWithMember(@Param(value = "id") Long id);
    
    @EntityGraph(attributePaths = {"replyEntities"})
    @Query("select p from PostEntity p where p.id = :id")
    Optional<PostEntity> findByIdWithReplies(@Param(value = "id") Long id);

    List<PostEntity> findAllByMemberEntityId(Long memberId);
}