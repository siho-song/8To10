package show.schedulemanagement.domain.notification;

import lombok.Getter;

@Getter
public enum NotificationType {
    REPLY_ADD("댓글 추가"),
    TODO_UPDATE("TODO 업데이트 촉구"),
    ACHIEVEMENT_FEEDBACK("성취도 피드백");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }
}
