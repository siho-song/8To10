package com.eighttoten.notification.service;

import static com.eighttoten.global.exception.ExceptionCode.FAILED_SSE_NOTIFICATION_SEND;

import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.domain.Notification;
import com.eighttoten.notification.domain.repository.SseEmitterRepository;
import com.eighttoten.notification.dto.NotificationResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseEmitterService {
    private static final Long DEFAULT_TIMEOUT = 2 * 60L * 1000 * 60;

    private final SseEmitterRepository sseEmitterRepository;

    private final NotificationService notificationService;

    public List<SseEmitter> findAllStartWithByMemberEmail(String email){
        return sseEmitterRepository.findAllStartWithByMemberEmail(email);
    }

    public SseEmitter subscribe(Member member, String lastEventId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        String uniqueEmitterId = generateUniqueClientId(member.getEmail(),LocalDateTime.now());
        sseEmitterRepository.save(uniqueEmitterId, emitter);

        emitter.onCompletion(() -> sseEmitterRepository.deleteById(uniqueEmitterId));
        emitter.onTimeout(() -> sseEmitterRepository.deleteById(uniqueEmitterId));
        emitter.onError((e) -> sseEmitterRepository.deleteById(uniqueEmitterId));

        sendToClient(emitter,uniqueEmitterId,"init","init");

        if(lastEventId != null){
            LocalDateTime dateTime = extractDateTime(lastEventId);
            List<Notification> notifications = notificationService.findAllAfterDateTime(dateTime, member);
            notifications.forEach(
                    notification -> sendToClient(emitter,
                            uniqueEmitterId,
                            "notification",
                            NotificationResponse.from(notification))
            );
        }

        return emitter;
    }

    public void sendToClient(SseEmitter emitter, String id, String name, Object data) {
        try {
            emitter.send(
                    SseEmitter.event()
                            .id(id)
                            .name(name)
                            .data(data)
            );
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            sseEmitterRepository.deleteById(id);
            throw new RuntimeException(FAILED_SSE_NOTIFICATION_SEND.getMessage());
        }
    }

    public LocalDateTime extractDateTime(String sseEmitterId) {
        long millis = Long.parseLong(sseEmitterId.substring(sseEmitterId.lastIndexOf("_") + 1));
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("UTC"));
    }

    public String generateUniqueClientId(String email , LocalDateTime localDateTime) {
        return email + "_" + localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}