package show.schedulemanagement.domain.notification;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import show.schedulemanagement.domain.auditing.baseentity.BaseTimeEntity;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.service.event.NotificationEvent;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
@DynamicInsert
public class Notification extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String message;

    @ColumnDefault(value = "false")
    private boolean isRead;

    private String targetUrl;

    private Long relatedEntityId;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private NotificationType notificationType;

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