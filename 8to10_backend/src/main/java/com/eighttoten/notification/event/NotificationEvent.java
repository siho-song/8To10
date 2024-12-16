package com.eighttoten.notification.event;

import com.eighttoten.notification.domain.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@ToString
public class NotificationEvent {
    String clientEmail;
    Long targetEntityId;
    Long relatedEntityId;
    String message;
    NotificationType notificationType;
}
