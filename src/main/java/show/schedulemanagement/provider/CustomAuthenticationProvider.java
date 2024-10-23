package show.schedulemanagement.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import show.schedulemanagement.dto.auth.MemberDetailsDto;
import show.schedulemanagement.service.auth.MemberDetailsService;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final MemberDetailsService memberDetailsService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = authentication.getCredentials().toString();

        MemberDetailsDto member = (MemberDetailsDto) memberDetailsService.loadUserByUsername(email);
        if (member == null || !bCryptPasswordEncoder.matches(password, member.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }

        return new UsernamePasswordAuthenticationToken(email, password, member.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
