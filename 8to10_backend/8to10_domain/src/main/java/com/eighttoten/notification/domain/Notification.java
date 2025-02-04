package com.eighttoten.notification.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class Notification {
    private Long id;
    private NotificationType notificationType;
    private String message;
    private String targetUrl;
    private Long relatedEntityId;
    private boolean readStatus;
    private String createdBy;

    public void updateReadStatus(boolean readStatus){
        this.readStatus = readStatus;
    }
}
