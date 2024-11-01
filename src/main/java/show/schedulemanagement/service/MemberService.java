package show.schedulemanagement.service;

import show.schedulemanagement.domain.member.Member;

public interface MemberService {
    Member findByEmail(String email);
    Member getAuthenticatedMember();
}
