package com.eighttoten.community.repository.post;

import com.eighttoten.community.PostEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostJpaRepository extends JpaRepository<PostEntity, Long>, PostRepositoryCustom {
    List<PostEntity> findAllByMemberEntityId(Long memberId);
}