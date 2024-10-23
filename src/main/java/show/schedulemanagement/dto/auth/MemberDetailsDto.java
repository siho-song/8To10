package show.schedulemanagement.dto.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.member.MemberRole;

import java.util.Collection;
import java.util.stream.Collectors;

@Getter
public class MemberDetailsDto implements UserDetails {

    private final Member member;

    public MemberDetailsDto(Member member) {
        this.member = member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getMemberRoles().stream()
                .map(MemberRole::getRole)
                .map(role -> (GrantedAuthority) role::toString)
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
