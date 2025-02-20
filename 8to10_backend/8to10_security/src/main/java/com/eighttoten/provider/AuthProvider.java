package com.eighttoten.provider;

import com.eighttoten.domain.MemberDetails;
import com.eighttoten.exception.CustomAuthenticationException;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.service.MemberDetailsService;
import com.eighttoten.support.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

@RequiredArgsConstructor
public class AuthProvider implements AuthenticationProvider {
    private final MemberDetailsService memberDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        MemberDetails member = (MemberDetails) memberDetailsService.loadUserByUsername(email);
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomAuthenticationException(ExceptionCode.INVALID_PASSWORD);
        }
        return new UsernamePasswordAuthenticationToken(member, password, member.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}