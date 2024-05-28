package show.schedulemanagement.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import show.schedulemanagement.domain.member.Member;

public interface MemberService {
    Member findByEmail(String email);
    Member loadUserByEmail(String email) throws UsernameNotFoundException;
    Member getAuthenticatedMember();
}
