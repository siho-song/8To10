package show.schedulemanagement.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.auth.MemberDetails;
import show.schedulemanagement.service.MemberService;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberService.findByEmail(email);

        log.debug("MemberDetailsService call loadUserByUsername : {}",member);
        if (member == null) {
            throw new UsernameNotFoundException("No member found with email: " + email);
        }
        return new MemberDetails(member);
    }
}
