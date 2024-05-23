package show.schedulemanagement.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.repository.member.MemberRepository;
import show.schedulemanagement.web.request.signup.SignUpRequestDto;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SignUpServiceImpl implements SignUpService{

    private final MemberRepository memberRepository;

    @Transactional
    public Member signUp(SignUpRequestDto signUpRequestDto){
        if (isValidSignUp(signUpRequestDto)){
            Member member = Member.of(signUpRequestDto);
            return memberRepository.save(member);
        }
        //TODO 오류구현
        return null;
    }

    public Boolean isDuplicatedEmail(String email){
        Member member = memberRepository.findByEmail(email).orElse(null);
        return member != null;
    }

    public Boolean isDuplicatedNickname(String nickname){
        Member member = memberRepository.findByNickname(nickname).orElse(null);
        return member != null;
    }

    private Boolean isValidSignUp(SignUpRequestDto signUpRequestDto) {
        return !isDuplicatedEmail(signUpRequestDto.getEmail()) && !isDuplicatedNickname(signUpRequestDto.getNickname());
    }
}
