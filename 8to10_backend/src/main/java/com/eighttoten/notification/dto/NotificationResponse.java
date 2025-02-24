package com.eighttoten.notification.dto;

import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.domain.NotificationType;
import lombok.Getter;

@Getter
public class NotificationResponse {
    private Long entityId;
    private String message;
    private Long relatedEntityId;
    private String targetUrl;
    private NotificationType notificationType;

    public static NotificationResponse from(Notification notification) {
        NotificationResponse notificationResponse = new NotificationResponse();
        notificationResponse.entityId = notification.getId();
        notificationResponse.message = notification.getMessage();
        notificationResponse.relatedEntityId = notification.getRelatedEntityId();
        notificationResponse.targetUrl = notification.getTargetUrl();
        notificationResponse.notificationType = notification.getNotificationType();
        return notificationResponse;
    }
}