package show.schedulemanagement.service.member;

import show.schedulemanagement.domain.member.Member;

public interface MemberService {
    Member findById(Long id);
    Member findByEmail(String email);
    Member getAuthenticatedMember();
}
