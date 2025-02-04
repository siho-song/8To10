package com.eighttoten.notification.domain;

import lombok.Getter;

@Getter
public class NotificationSendInfo {
    private Long entityId;
    private String message;
    private Long relatedEntityId;
    private String targetUrl;
    private NotificationType notificationType;

    public static NotificationSendInfo from(Notification notification) {
        NotificationSendInfo notificationSendInfo = new NotificationSendInfo();
        notificationSendInfo.entityId = notification.getId();
        notificationSendInfo.message = notification.getMessage();
        notificationSendInfo.relatedEntityId = notification.getRelatedEntityId();
        notificationSendInfo.targetUrl = notification.getTargetUrl();
        notificationSendInfo.notificationType = notification.getNotificationType();
        return notificationSendInfo;
    }
}