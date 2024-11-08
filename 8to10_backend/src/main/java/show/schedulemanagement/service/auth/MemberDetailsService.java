package show.schedulemanagement.service.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import show.schedulemanagement.domain.auth.MemberDetails;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.exception.ExceptionCode;
import show.schedulemanagement.exception.UserAuthenticationException;
import show.schedulemanagement.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UserAuthenticationException(ExceptionCode.INVALID_EMAIL));
        return new MemberDetails(member);
    }
}
