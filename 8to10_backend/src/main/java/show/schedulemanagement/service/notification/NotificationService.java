package show.schedulemanagement.service.notification;

import static show.schedulemanagement.exception.ExceptionCode.INVALID_REDIS_MESSAGE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.notification.Notification;
import show.schedulemanagement.dto.NotificationResponse;
import show.schedulemanagement.exception.InvalidRedisMessageException;
import show.schedulemanagement.repository.notification.NotificationRepository;
import show.schedulemanagement.service.event.NotificationEvent;
import show.schedulemanagement.service.member.MemberService;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private static final String EVENT_NAME = "notification";

    private final NotificationRepository notificationRepository;
    private final MemberService memberService;
    private final SseEmitterService sseEmitterService;

    private final RedisOperations<String,String> redisOperations;
    private final ChannelTopic notificationTopic;
    private final ObjectMapper objectMapper;

    @Transactional
    public void handleNotificationEvent(String message) {
        NotificationEvent event = convertToObject(message, NotificationEvent.class);

        Member member = memberService.findByEmail(event.getClientEmail());
        //emitters
        List<SseEmitter> emitters = sseEmitterService.findAllStartWithByMemberEmail(member.getEmail());
        if(!emitters.isEmpty()){
            Notification notification = Notification.from(member,event);
            notificationRepository.save(notification);
            String response = convertToJson(NotificationResponse.from(notification));
            emitters.forEach(emitter -> sseEmitterService.sendToClient(
                    emitter,
                    sseEmitterService.generateUniqueClientId(member.getEmail(), LocalDateTime.now()),
                    EVENT_NAME,
                    response)
            );
        }
    }

    @Transactional(readOnly = true)
    public void handleLastEventId(String message){
        String email = sseEmitterService.extractClientEmail(message);
        Member member = memberService.findByEmail(email);

        List<SseEmitter> emitters = sseEmitterService.findAllStartWithByMemberEmail(email); //이게 빈 객체라면 애초에 들어온 요청이 잘 못 된건데

        if(emitters.isEmpty()){
            log.warn("해당 이메일에 대한 SSE 연결을 찾을 수 없습니다. 이메일 : {}", email);
            return;
        }
        //알람 중복 전송 ?
        for (SseEmitter emitter : emitters) {
            //해당 유저의 모든 디바이스
            //핸드폰, 테블릿, 테블릿
            //여기서 들어온 lastEventId 는 ?
            List<Notification> notifications = notificationRepository.findNewerNotifications(
                    sseEmitterService.extractDateTime(message),member);
            for (Notification notification : notifications) {
                String response = convertToJson(NotificationResponse.from(notification));
                String id = sseEmitterService.generateUniqueClientId(email, notification.getCreatedAt());
                sseEmitterService.sendToClient(
                        emitter,
                        id,
                        EVENT_NAME,
                        response
                );
            }
        }
    }

    @Async
    @EventListener
    public void publishNotificationEvent(NotificationEvent event){
        String channelEvent = convertToJson(event);
        redisOperations.convertAndSend(notificationTopic.getTopic(), channelEvent);
    }

    private String convertToJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
            throw new InvalidRedisMessageException(INVALID_REDIS_MESSAGE);
        }
    }

    private <T> T convertToObject(String message, Class<T> classType) {
        try {
            return objectMapper.readValue(message.getBytes(), classType);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new InvalidRedisMessageException(INVALID_REDIS_MESSAGE);
        }
    }
}
