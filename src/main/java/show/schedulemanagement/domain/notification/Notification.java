package show.schedulemanagement.domain.notification;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import show.schedulemanagement.domain.member.Member;

@Entity
@Getter
@Builder
@AllArgsConstructor
@Table(name = "NOTIFICATION")
public class Notification{
    @Id @GeneratedValue(strategy = IDENTITY)
    @Column(name = "notification_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT")
    private String message;

    private boolean is_read;
    private Long entityId;
    private String type;


    protected Notification(){
        is_read = false;
    }
}