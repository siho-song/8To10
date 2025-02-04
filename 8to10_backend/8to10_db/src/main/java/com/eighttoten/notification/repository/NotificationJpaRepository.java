package com.eighttoten.notification.repository;

import com.eighttoten.notification.NotificationEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("select n from NotificationEntity n where n.createdAt > :dateTime and n.memberEntity.id= :memberId")
    List<NotificationEntity> findAllByMemberIdAfter(@Param("memberId") Long memberId, @Param("dateTime") LocalDateTime dateTime);
}
