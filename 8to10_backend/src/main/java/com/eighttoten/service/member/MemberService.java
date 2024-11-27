package com.eighttoten.service.member;

import java.util.List;
import com.eighttoten.domain.member.Member;

public interface MemberService {
    Member findById(Long id);
    Member findByEmail(String email);
    List<Member> findAll();
    Member getAuthenticatedMember();
}
