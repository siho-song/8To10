package show.schedulemanagement;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.service.MemberService;

@Configuration
public class TestConfig {
    @Autowired
    MemberService memberService;

    @PostConstruct
    public void init(){
        MemberDetailsDto user = new MemberDetailsDto(memberService.loadUserByEmail("normal@example.com"));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
