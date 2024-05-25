package show.schedulemanagement.domain.member;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import show.schedulemanagement.domain.baseEntity.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@ToString(exclude = {"member"})
public class MemberRole extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_role_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "NORMAL_USER")
    private Role role;

    private void setRole(Member member, Role role){
        this.role= role;
        member.getRoles().add(this);
    }

    public static MemberRole createMemberRoleInfo(Member member, Role role){
        MemberRole memberRole = MemberRole.builder()
                .member(member)
                .build();

        memberRole.setRole(member,role);
        return memberRole;
    }
}