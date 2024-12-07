package com.eighttoten.service.auth;

import com.eighttoten.domain.auth.MemberDetails;
import com.eighttoten.domain.member.Member;
import com.eighttoten.exception.AuthException;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new AuthException(ExceptionCode.INVALID_EMAIL));
        return new MemberDetails(member);
    }
}