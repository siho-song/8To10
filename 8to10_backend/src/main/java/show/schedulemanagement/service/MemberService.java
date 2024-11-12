package show.schedulemanagement.service;

import show.schedulemanagement.domain.member.Member;

public interface MemberService {
    Member findById(Long id);
    Member findByEmail(String email);
    Member getAuthenticatedMember();
}
