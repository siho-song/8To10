package com.eighttoten.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.eighttoten.domain.notification.Notification;
import com.eighttoten.domain.notification.NotificationType;

@NoArgsConstructor
@Getter
public class NotificationResponse {

    String message;
    Long relatedEntityId;
    String targetUrl;
    NotificationType notificationType;

    public static NotificationResponse from(Notification notification) {
        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.message = notification.getMessage();
        notificationResponse.relatedEntityId = notification.getRelatedEntityId();
        notificationResponse.targetUrl = notification.getTargetUrl();
        notificationResponse.notificationType = notification.getNotificationType();
        return notificationResponse;
    }
}
