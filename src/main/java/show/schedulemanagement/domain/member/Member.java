package show.schedulemanagement.domain.member;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "MEMBER")
public class Member {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String username;
    private String nickname;
    private String email;
    private String password;

    @Enumerated(value = STRING)
    private Gender gender;
    @Enumerated(value = STRING)
    private Mode mode;
    @Enumerated(value = STRING)
    private Role role;

    private String imageFile;
    private Double score;

    protected Member(){
        role = Role.NORMAL_USER;
    }
}
