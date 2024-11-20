package show.schedulemanagement.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import show.schedulemanagement.config.web.CurrentMember;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.service.notification.SseEmitterService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
@Slf4j
public class NotificationController {
    private final SseEmitterService sseEmitterService;

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(
            @CurrentMember Member member,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {
        return ResponseEntity.ok(sseEmitterService.subscribe(member, lastEventId));
    }
}