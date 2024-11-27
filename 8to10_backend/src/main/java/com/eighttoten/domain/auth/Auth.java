package com.eighttoten.domain.auth;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import com.eighttoten.domain.auditing.baseentity.BaseEntity;

@Entity
@Getter
@Builder
@AllArgsConstructor
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

    public static Auth from(String memberEmail, String refreshToken) {
        return Auth.builder()
                .email(memberEmail)
                .refreshToken(refreshToken)
                .build();
    }
}