package show.schedulemanagement.service.member;

import java.util.List;
import show.schedulemanagement.domain.member.Member;

public interface MemberService {
    Member findById(Long id);
    Member findByEmail(String email);
    List<Member> findAll();
    Member getAuthenticatedMember();
}
