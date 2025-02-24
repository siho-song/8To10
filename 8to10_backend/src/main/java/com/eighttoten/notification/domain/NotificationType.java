package com.eighttoten.notification.domain;

import lombok.Getter;

@Getter
public enum NotificationType {
    REPLY_ADD("댓글 추가", "/board",true),
    NESTED_REPLY_ADD("대댓글 추가", "/board", true),
    TODO_UPDATE("TODO 업데이트", null, true),
    ACHIEVEMENT_FEEDBACK("성취도", null, true),
    ;

    private final String value;
    private final String baseTargetUrl;
    private final Boolean isNeededSave;

    NotificationType(String value, String baseTargetUrl,Boolean isNeededSave) {
        this.value = value;
        this.baseTargetUrl = baseTargetUrl;
        this.isNeededSave = isNeededSave;
    }
}