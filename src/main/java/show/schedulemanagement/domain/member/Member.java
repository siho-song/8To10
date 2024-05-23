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
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.baseEntity.BaseEntity;
import show.schedulemanagement.web.request.signup.SignUpRequestDto;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@Table(name = "MEMBER")
@DynamicInsert
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

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private Gender gender;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    private Mode mode;

    @Enumerated(value = STRING)
    @Column(nullable = false)
    @ColumnDefault(value = "NORMAL_USER")
    private Role role;

    @Column(nullable = false)
    @ColumnDefault(value = "xxx") //TODO 기본 이미지 파일 생성 후 , value 값 수정
    private String imageFile;

    @Column(nullable = false)
    @ColumnDefault(value = "0")
    private Double score;

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    //TODO 비밀번호 암호화
    public static Member of(SignUpRequestDto signUpRequestDto){
        return Member.builder()
                .username(signUpRequestDto.getUsername())
                .nickname(signUpRequestDto.getNickname())
                .email(signUpRequestDto.getEmail())
                .password(signUpRequestDto.getPassword())
                .gender(signUpRequestDto.getGender())
                .mode(signUpRequestDto.getMode())
                .role(Role.NORMAL_USER)
                .build();
    }
}
