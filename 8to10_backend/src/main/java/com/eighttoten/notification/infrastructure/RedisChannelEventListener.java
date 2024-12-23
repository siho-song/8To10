package com.eighttoten.notification.infrastructure;

import static com.eighttoten.global.exception.ExceptionCode.INVALID_REDIS_MESSAGE;

import com.eighttoten.global.exception.InvalidRedisMessageException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.service.MemberService;
import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.dto.NotificationResponse;
import com.eighttoten.notification.event.NotificationEvent;
import com.eighttoten.notification.service.NotificationService;
import com.eighttoten.notification.service.SseEmitterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RequiredArgsConstructor
@Component
@Slf4j
public class RedisChannelEventListener {
    private static final String NOTIFICATION_EVENT_NAME = "notification";

    private final NotificationService notificationService;
    private final MemberService memberService;
    private final SseEmitterService sseEmitterService;

    private final ObjectMapper objectMapper;

    public void handleNotificationEvent(String message) {
        NotificationEvent event = convertToObject(message, NotificationEvent.class);

        Member member = memberService.findByEmail(event.getClientEmail());

        Notification notification = Notification.from(member, event);
        if (notification.getNotificationType().getIsNeededSave()) {
            notificationService.save(notification);
        }

        List<SseEmitter> emitters = sseEmitterService.findAllStartWithByMemberEmail(member.getEmail());
        if (!emitters.isEmpty()) {
            String response = convertToJson(NotificationResponse.from(notification));
            emitters.forEach(emitter -> sseEmitterService.sendToClient(
                    emitter,
                    sseEmitterService.generateUniqueClientId(member.getEmail(), LocalDateTime.now()),
                    NOTIFICATION_EVENT_NAME,
                    response)
            );
        }
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