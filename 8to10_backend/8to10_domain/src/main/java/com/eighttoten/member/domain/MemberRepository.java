package com.eighttoten.member.domain;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    void save(NewMember newMember);
    void update(Member member);
    void deleteById(Long id);
    Optional<Member> findById(Long id);
    Optional<Member> findByEmail(String email);
    Optional<Member> findByNickname(String nickname);
    List<Member> findAll();
}