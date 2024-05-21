package show.schedulemanagement.domain.authentication;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import show.schedulemanagement.domain.member.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "AUTHENTICATION")
public class Authentication {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "authentication_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private boolean auth_email;
    private boolean auth_phone;

    protected Authentication() {
        auth_email = false;
        auth_phone = false;
    }
}
