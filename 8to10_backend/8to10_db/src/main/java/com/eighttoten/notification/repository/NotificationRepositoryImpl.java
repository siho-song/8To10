package com.eighttoten.notification.repository;

import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.MemberEntity;
import com.eighttoten.member.repository.MemberJpaRepository;
import com.eighttoten.notification.NotificationEntity;
import com.eighttoten.notification.domain.NewNotification;
import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.domain.repository.NotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryImpl implements NotificationRepository {
    private final NotificationJpaRepository notificationRepository;
    private final MemberJpaRepository memberJpaRepository;

    @Override
    public long save(NewNotification newNotification) {
        MemberEntity memberEntity = memberJpaRepository.findById(newNotification.getMemberId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_MEMBER));
        return notificationRepository.save(NotificationEntity.from(newNotification, memberEntity)).getId();
    }

    @Override
    public void update(Notification notification) {
        NotificationEntity entity = notificationRepository.findById(notification.getId())
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_NOTIFICATION));
        entity.update(notification);
    }

    @Override
    public void deleteById(Long id) {
        notificationRepository.deleteById(id);
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationRepository.findById(id).map(NotificationEntity::toNotification);
    }

    @Override
    public List<Notification> findAllByMemberIdAfterStart(Long memberId, LocalDateTime start) {
        List<NotificationEntity> entities = notificationRepository.findAllByMemberIdAfterStart(memberId, start);
        return entities.stream().map(NotificationEntity::toNotification).toList();
    }
}
