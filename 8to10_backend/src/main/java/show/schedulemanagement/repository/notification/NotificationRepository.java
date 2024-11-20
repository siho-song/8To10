package show.schedulemanagement.repository.notification;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.notification.Notification;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    @Query("select n from Notification n where n.createdAt > :dateTime and n.member= :member")
    List<Notification> findNewerNotifications(@Param("dateTime") LocalDateTime dateTime, Member member);

}
