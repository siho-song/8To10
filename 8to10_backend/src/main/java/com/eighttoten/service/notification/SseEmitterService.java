package com.eighttoten.service.notification;

import static com.eighttoten.exception.ExceptionCode.FAILED_SSE_NOTIFICATION_SEND;

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
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.notification.Notification;
import com.eighttoten.dto.NotificationResponse;
import com.eighttoten.exception.SseSendException;
import com.eighttoten.repository.notification.SseEmitterRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseEmitterService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

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
            throw new SseSendException(FAILED_SSE_NOTIFICATION_SEND.getMessage());
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