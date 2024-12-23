package com.eighttoten.member.service;

import static com.eighttoten.global.exception.ExceptionCode.SIGNUP_FAILED;

import com.eighttoten.global.exception.BadRequestException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.repository.MemberRepository;
import com.eighttoten.member.dto.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
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