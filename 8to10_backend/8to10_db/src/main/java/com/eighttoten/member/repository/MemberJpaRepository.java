package com.eighttoten.member.repository;

import com.eighttoten.member.MemberEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<MemberEntity,Long> {
    Optional<MemberEntity> findByEmail(String email);
    Optional<MemberEntity> findByNickname(String nickname);
}