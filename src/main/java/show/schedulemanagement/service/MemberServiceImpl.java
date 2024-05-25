package show.schedulemanagement.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public Member findByEmail(String email) {
        Member findMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("No member found with email: \" " + email));

        log.debug("findByEmail : findMember = {}",findMember);
        return findMember;
    }

    public Member loadUserByEmail(String email) throws UsernameNotFoundException {
        return memberRepository.findWithRolesByEmail(email).orElseThrow(()->new UsernameNotFoundException("Member email not found."));
    }
}
