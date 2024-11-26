package show.schedulemanagement.service.notification;

import static show.schedulemanagement.exception.ExceptionCode.INVALID_REDIS_MESSAGE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.notification.Notification;
import show.schedulemanagement.exception.ExceptionCode;
import show.schedulemanagement.exception.InvalidRedisMessageException;
import show.schedulemanagement.exception.NotFoundEntityException;
import show.schedulemanagement.repository.notification.NotificationRepository;
import show.schedulemanagement.service.event.NotificationEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationRepository notificationRepository;

    private final RedisOperations<String,String> redisOperations;
    private final ChannelTopic notificationTopic;
    private final ObjectMapper objectMapper;

    @Transactional
    public void save(Notification notification){
        notificationRepository.save(notification);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        Notification notification = notificationRepository.findByIdWithMember(id)
                .orElseThrow(() -> new NotFoundEntityException(ExceptionCode.NOT_FOUND_NOTIFICATION));
        if(member.isSameEmail(notification.getMember().getEmail())){
            notificationRepository.deleteById(id);
        }
    }

    @Transactional(readOnly = true)
    public List<Notification> findAllAfterDateTime(LocalDateTime dateTime, Member member) {
        return notificationRepository.findAllAfterDateTime(dateTime, member);
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
