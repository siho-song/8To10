package com.eighttoten.support;

import com.eighttoten.member.domain.Member;
import com.eighttoten.service.MemberDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthAccessorImpl implements AuthAccessor{
    private final MemberDetailsService memberDetailsService;

    @Override
    public Member getAuthenticatedMember() {
        return memberDetailsService.getAuthenticatedMember();
    }
}
