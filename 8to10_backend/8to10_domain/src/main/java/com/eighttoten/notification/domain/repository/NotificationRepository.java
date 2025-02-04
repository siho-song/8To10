package com.eighttoten.notification.domain.repository;

import com.eighttoten.notification.domain.NewNotification;
import com.eighttoten.notification.domain.Notification;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NotificationRepository {
    void deleteById(Long id);
    long save(NewNotification newNotification);
    void update(Notification notification);
    Optional<Notification> findById(Long id);
    List<Notification> findAllByMemberIdAfter(Long memberId, LocalDateTime dateTime);
}