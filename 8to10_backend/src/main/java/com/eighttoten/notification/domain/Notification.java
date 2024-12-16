package com.eighttoten.notification.domain;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import com.eighttoten.global.auditing.baseentity.BaseTimeEntity;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.event.NotificationEvent;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = PROTECTED)
public class Notification extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String targetUrl;

    private Long relatedEntityId;

    private boolean isRead;

    public static Notification from(Member member, NotificationEvent event){
        Notification notification = new Notification();
        notification.member = member;
        notification.message = event.getMessage();
        notification.isRead = false;

        NotificationType notificationType = event.getNotificationType();
        notification.notificationType = notificationType;

        notification.setTargetUrl(notificationType.getBaseTargetUrl(),event.getTargetEntityId());
        notification.relatedEntityId = event.getRelatedEntityId();

        return notification;
    }

    public void updateIsRead(){
        this.isRead = true;
    }

    private void setTargetUrl(String baseTargetUrl, Long targetEntityId){
        this.targetUrl = baseTargetUrl + "/" + targetEntityId;
    }
}