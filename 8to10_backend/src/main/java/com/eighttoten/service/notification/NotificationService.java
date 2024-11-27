package com.eighttoten.service.notification;

import static com.eighttoten.exception.ExceptionCode.INVALID_REDIS_MESSAGE;
import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_NOTIFICATION;

import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.notification.Notification;
import com.eighttoten.exception.InvalidRedisMessageException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.repository.notification.NotificationRepository;
import com.eighttoten.service.event.NotificationEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final RedisOperations<String,String> redisOperations;
    private final ChannelTopic notificationTopic;
    private final ObjectMapper objectMapper;

    public Notification findByIdWithMember(Long id) {
        return notificationRepository.findByIdWithMember(id).orElseThrow(()->new NotFoundEntityException(
                NOT_FOUND_NOTIFICATION));
    }

    public List<Notification> findAllAfterDateTime(LocalDateTime dateTime, Member member) {
        return notificationRepository.findAllAfterDateTime(dateTime, member);
    }

    @Transactional
    public void save(Notification notification){
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        Notification notification = findByIdWithMember(id);
        if(member.isSameEmail(notification.getMember().getEmail())){
            notificationRepository.deleteById(id);
        }
    }

    @Transactional
    public void updateReadStatus(Member member, Long id) {
        Notification notification = findByIdWithMember(id);
        if (member.isSameEmail(notification.getMember().getEmail())) {
            notification.updateIsRead();
        }
    }

    @Async
    @EventListener
    public void publishNotificationEvent(NotificationEvent event){
        try {
            String channelEvent = objectMapper.writeValueAsString(event);
            redisOperations.convertAndSend(notificationTopic.getTopic(), channelEvent);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new InvalidRedisMessageException(INVALID_REDIS_MESSAGE);
        }
    }
}
