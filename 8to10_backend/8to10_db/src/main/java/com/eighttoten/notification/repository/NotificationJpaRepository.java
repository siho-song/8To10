package com.eighttoten.notification.repository;

import com.eighttoten.notification.NotificationEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationJpaRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("select n from NotificationEntity n where n.createdAt > :start and n.memberEntity.id= :memberId")
    List<NotificationEntity> findAllByMemberIdAfterStart(@Param("memberId") Long memberId, @Param("start") LocalDateTime start);
}
