package com.eighttoten.notification.domain.repository;

import com.eighttoten.notification.domain.NewNotification;
import com.eighttoten.notification.domain.Notification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    long save(NewNotification newNotification);
    void update(Notification notification);
    void deleteById(Long id);
    Optional<Notification> findById(Long id);
    List<Notification> findAllByMemberIdAfterStart(Long memberId, LocalDateTime start);
}