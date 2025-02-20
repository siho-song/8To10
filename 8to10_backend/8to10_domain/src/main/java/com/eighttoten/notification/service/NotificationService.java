package com.eighttoten.notification.service;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_NOTIFICATION;

import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.domain.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationService {
    private final NotificationRepository notificationRepository;

    @Transactional
    public void deleteById(Member member, Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_NOTIFICATION));

        member.checkIsSameEmail(notification.getCreatedBy());
        notificationRepository.deleteById(id);
    }

    @Transactional
    public void updateReadStatus(Member member, Long id, boolean readStatus) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_NOTIFICATION));

        member.checkIsSameEmail(notification.getCreatedBy());
        notification.updateReadStatus(readStatus);
        notificationRepository.update(notification);
    }
}
