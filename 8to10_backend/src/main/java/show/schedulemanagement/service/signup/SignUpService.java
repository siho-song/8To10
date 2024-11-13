package show.schedulemanagement.service.signup;

import static show.schedulemanagement.exception.ExceptionCode.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.signup.SignUpRequest;
import show.schedulemanagement.exception.BadRequestException;
import show.schedulemanagement.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SignUpService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signUp(SignUpRequest signUpRequest){
        if (!isValidSignUp(signUpRequest)){
            throw new BadRequestException(SIGNUP_FAILED);
        }
        encodePassword(signUpRequest);
        Member member = Member.from(signUpRequest);
        memberRepository.save(member);
    }

    public Boolean isValidSignUp(SignUpRequest signUpRequest) {
        return !isDuplicatedEmail(signUpRequest.getEmail()) && !isDuplicatedNickname(signUpRequest.getNickname());
    }

    public Boolean isDuplicatedEmail(String email){
        Member member = memberRepository.findByEmail(email)
                .orElse(null);

        return member != null;
    }

    public Boolean isDuplicatedNickname(String nickname){
        Member member = memberRepository.findByNickname(nickname)
                .orElse(null);

        return member != null;
    }

    private void encodePassword(SignUpRequest signUpRequest) {
        signUpRequest.setPassword(bCryptPasswordEncoder.encode(signUpRequest.getPassword()));
    }
}
