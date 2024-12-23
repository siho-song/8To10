package com.eighttoten.global.auditing;

import com.eighttoten.infrastructure.security.domain.MemberDetails;
import com.eighttoten.member.domain.Member;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        MemberDetails memberDetails = (MemberDetails) principal;
        Member member = memberDetails.getMember();

        return Optional.of(member.getEmail());
    }
}