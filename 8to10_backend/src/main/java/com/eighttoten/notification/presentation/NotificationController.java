package com.eighttoten.notification.presentation;

import com.eighttoten.global.CurrentMember;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.service.NotificationService;
import com.eighttoten.notification.service.SseEmitterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notification")
public class NotificationController {
    private final SseEmitterService sseEmitterService;
    private final NotificationService notificationService;

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@CurrentMember Member member, @PathVariable(value = "id") Long id) {
        notificationService.deleteById(member, id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@CurrentMember Member member, @PathVariable(value = "id") Long id) {
        notificationService.updateReadStatus(member,id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(
            @CurrentMember Member member,
            @RequestHeader(value = "Last-Event-ID", required = false) String lastEventId) {
        return ResponseEntity.ok(sseEmitterService.subscribe(member, lastEventId));
    }
}