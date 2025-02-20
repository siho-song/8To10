package com.eighttoten.member;

import static jakarta.persistence.EnumType.STRING;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.BaseEntity;
import com.eighttoten.member.domain.Gender;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.Mode;
import com.eighttoten.member.domain.NewMember;
import com.eighttoten.member.domain.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
@Table(name = "member")
public class MemberEntity extends BaseEntity {
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

    private String profileImagePath;

    private double score;

    private boolean authEmail;

    private boolean authPhone;

    public static MemberEntity from(NewMember newMember) {
        MemberEntity memberEntity = new MemberEntity();
        memberEntity.username = newMember.getUsername();
        memberEntity.nickname = newMember.getNickname();
        memberEntity.email = newMember.getEmail();
        memberEntity.password = newMember.getPassword();
        memberEntity.phoneNumber = newMember.getPhoneNumber();
        memberEntity.gender = newMember.getGender();
        memberEntity.role = newMember.getRole();
        memberEntity.mode = newMember.getMode();
        memberEntity.authEmail = newMember.isAuthEmail();
        memberEntity.authPhone = newMember.isAuthPhone();
        return memberEntity;
    }

    public Member toMember() {
        return new Member(id, username, nickname, email,
                password, phoneNumber, gender, Role.NORMAL_USER, mode,
                profileImagePath, score, authEmail, authPhone);
    }

    @PrePersist
    public void prePersist() {
        this.createdBy = "ADMIN"; // 또는 다른 값
        this.updatedBy = "ADMIN"; // 또는 다른 값
    }

    public void update(Member member) {
        nickname = member.getNickname();
        password = member.getPassword();
        profileImagePath = member.getProfileImagePath();
    }
}