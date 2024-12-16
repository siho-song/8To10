package com.eighttoten.infrastructure.security.service;

import com.eighttoten.global.exception.ExceptionCode;
import com.eighttoten.infrastructure.security.domain.MemberDetails;
import com.eighttoten.infrastructure.security.exception.AuthException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.repository.MemberRepository;
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