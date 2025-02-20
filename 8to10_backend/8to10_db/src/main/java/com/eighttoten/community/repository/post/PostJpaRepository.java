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
    List<PostEntity> findAllByMemberEntityId(Long memberId);
}