package show.schedulemanagement.service.signup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.member.MemberRole;
import show.schedulemanagement.domain.member.Role;
import show.schedulemanagement.dto.signup.SignUpRequest;
import show.schedulemanagement.repository.member.MemberRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SignUpServiceImpl implements SignUpService{

    private final MemberRepository memberRepository;
    private final SignUpValidationService signUpValidationService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void signUp(SignUpRequest signUpRequest){
        if (signUpValidationService.isValidSignUp(signUpRequest)){
            encodePassword(signUpRequest);
            Member member = Member.of(signUpRequest);
            MemberRole.createMemberRoleInfo(member, Role.NORMAL_USER);
            Member save = memberRepository.save(member);
            log.info("save member : {}", save);
            return;
        }

        //TODO 오류구현
        throw new IllegalArgumentException("회원가입 실패");
    }

    private void encodePassword(SignUpRequest signUpRequest) {
        String password = signUpRequest.getPassword();
        signUpRequest.setPassword(bCryptPasswordEncoder.encode(password));
    }
}
