package com.eighttoten.domain.member;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import com.eighttoten.domain.auditing.baseentity.BaseEntity;
import com.eighttoten.dto.signup.SignUpRequest;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
@DynamicInsert
@Builder
@ToString
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

    private String imageFile;

    private double score;

    private boolean authEmail;

    private boolean authPhone;

    public void changeNickname(String nickname){
        this.nickname = nickname;
    }

    public static Member from(SignUpRequest signUpRequest){
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

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePassword(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void updateProfilePhoto(String filePath) {
        this.imageFile = filePath;
    }
}