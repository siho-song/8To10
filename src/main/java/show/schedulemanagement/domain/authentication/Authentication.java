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
import org.hibernate.annotations.ColumnDefault;
import show.schedulemanagement.domain.baseEntity.BaseEntity;
import show.schedulemanagement.domain.member.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
public class Authentication extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "authentication_id")
    private Long id;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean authEmail;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean authPhone;

    protected Authentication() {
        authEmail = false;
        authPhone = false;
    }
}
