package show.schedulemanagement.domain.member;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Collate;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.auditing.baseentity.BaseEntity;
import show.schedulemanagement.dto.signup.SignUpRequest;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "MEMBER")
@DynamicInsert
@ToString(exclude = {"memberRoles"})
@Builder
public class Member extends BaseEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private Mode mode;

    @Column(nullable = false)
    @ColumnDefault(value = "xxx") //TODO 기본 이미지 파일 생성 후 , value 값 수정
    private String imageFile;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Double score;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean authEmail;

    @Column(nullable = false)
    @ColumnDefault(value = "false")
    private boolean authPhone;

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public static Member of(SignUpRequest signUpRequest){
        return Member.builder()
                .username(signUpRequest.getUsername())
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .password(signUpRequest.getPassword())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .gender(signUpRequest.getGender())
                .mode(signUpRequest.getMode())
                .role(Role.NORMAL_USER)
                .authEmail(signUpRequest.getAuthEmail())
                .authPhone(signUpRequest.getAuthPhone())
                .build();
    }

    @PrePersist
    public void prePersist() {
        this.createdBy = "ADMIN"; // 또는 다른 값
        this.updatedBy = "ADMIN"; // 또는 다른 값
    }

    public boolean isSameEmail(String email) {
        return this.email.equals(email);
    }
}