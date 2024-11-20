package show.schedulemanagement.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    REPLY_ADD("댓글 추가", "/board"),
    NESTED_REPLY_ADD("대댓글 추가", "/board"),
    TODO_UPDATE("TODO 업데이트", null),
    ACHIEVEMENT_FEEDBACK("성취도", null),
    ;

    private final String value;
    private final String baseTargetUrl;

    NotificationType(String value, String baseTargetUrl) {
        this.value = value;
        this.baseTargetUrl = baseTargetUrl;
    }
}
