package show.schedulemanagement.service.notification;

import static show.schedulemanagement.exception.ExceptionCode.FAILED_SSE_NOTIFICATION_SEND;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.exception.SseSendException;
import show.schedulemanagement.repository.notification.SseEmitterRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseEmitterService {
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 60;

    private final SseEmitterRepository sseEmitterRepository;

    private final RedisOperations<String,String> redisOperations;
    private final ChannelTopic lastEventIdTopic;

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

        try {
            emitter.send(
                    SseEmitter.event()
                            .id(uniqueEmitterId)
                            .name("init")
                            .data("init")
            );
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            sseEmitterRepository.deleteById(uniqueEmitterId);
            throw new SseSendException(FAILED_SSE_NOTIFICATION_SEND);
        }

        if(lastEventId != null){
            redisOperations.convertAndSend(lastEventIdTopic.getTopic(), lastEventId);
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
            throw new SseSendException(FAILED_SSE_NOTIFICATION_SEND);
        }
    }

    public String extractClientEmail(String sseEmitterId) {
        return sseEmitterId.substring(0, sseEmitterId.lastIndexOf("_"));
    }

    public LocalDateTime extractDateTime(String sseEmitterId) {
        long millis = Long.parseLong(sseEmitterId.substring(sseEmitterId.lastIndexOf("_") + 1));
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(millis), ZoneId.of("UTC"));
    }

    public String generateUniqueClientId(String email , LocalDateTime localDateTime) {
        return email + "_" + localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
    }
}
