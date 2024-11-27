package show.schedulemanagement.service.member;

import static show.schedulemanagement.exception.ExceptionCode.NOT_FOUND_MEMBER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.auth.MemberDetails;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.exception.NotFoundEntityException;
import show.schedulemanagement.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    @Override
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Member getAuthenticatedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        return principal.getMember();
    }
}
