package com.eighttoten.domain.auth;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.domain.auditing.baseentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Auth extends BaseEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "auth_id")
    private Long id;

    private String email;

    private String refreshToken;

    @PrePersist
    void prePersist(){
        this.createdBy = "ADMIN";
        this.updatedBy = "ADMIN";
    }

    public static Auth of(String memberEmail, String refreshToken) {
        Auth auth = new Auth();
        auth.email = memberEmail;
        auth.refreshToken = refreshToken;
        return auth;
    }
}