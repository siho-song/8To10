package show.schedulemanagement.domain.member;

import lombok.Getter;

@Getter
public enum Role {

    ADMIN("관리자"),NORMAL_USER("일반 유저"), FAITHFUL_USER("일정을 잘 지키는 유저");

    private final String role;

    Role(String role) {
        this.role = role;
    }
}
