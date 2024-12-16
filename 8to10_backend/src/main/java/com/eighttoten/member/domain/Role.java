package com.eighttoten.member.domain;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("관리자"),NORMAL_USER("일반 유저"), FAITHFUL_USER("성실 유저");

    private final String value;

    Role(String value) {
        this.value = value;
    }
}