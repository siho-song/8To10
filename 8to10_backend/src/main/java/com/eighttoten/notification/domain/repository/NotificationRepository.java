package com.eighttoten.notification.domain.repository;

import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.domain.Notification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    @EntityGraph(attributePaths = "member")
    @Query("select n from Notification n where n.id = :id")
    Optional<Notification> findByIdWithMember(@Param(value = "id") Long Id);

    @Query("select n from Notification n where n.createdAt > :dateTime and n.member= :member")
    List<Notification> findAllAfterDateTime(@Param("dateTime") LocalDateTime dateTime, Member member);
}
