package com.eighttoten.member.service;

import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_MEMBER;

import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.infrastructure.security.domain.MemberDetails;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.repository.MemberRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_MEMBER));
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member getAuthenticatedMember() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        MemberDetails principal = (MemberDetails) authentication.getPrincipal();
        return principal.getMember();
    }
}