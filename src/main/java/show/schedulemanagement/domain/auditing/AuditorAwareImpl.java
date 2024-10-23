package show.schedulemanagement.domain.auditing;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.auth.MemberDetailsDto;

@Service
@Slf4j
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        log.debug("AuditorAwareImpl authentication : {} " , authentication);
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        log.debug("AuditorAwareImpl principal1 : {} " , principal);
        MemberDetailsDto memberDetailsDto = (MemberDetailsDto) principal;
        Member member = memberDetailsDto.getMember();

        return Optional.of(member.getEmail());
    }
}
