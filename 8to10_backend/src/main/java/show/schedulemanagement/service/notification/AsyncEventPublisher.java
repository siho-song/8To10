package show.schedulemanagement.service.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.domain.notification.NotificationType;
import show.schedulemanagement.service.event.NotificationEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class AsyncEventPublisher {
    private final ApplicationEventPublisher eventPublisher;

    @Async
    public void publishReplyAddEvent(Board board, Reply reply) {
        Member boardWriter = board.getMember();
        Member replyWriter = reply.getMember();

        if(reply.getParent() != null){
            eventPublisher.publishEvent(new NotificationEvent(
                    replyWriter.getEmail(),
                    board.getId(),
                    reply.getId(),
                    NotificationMessage.NESTED_REPLY_ADD.getMessage(),
                    NotificationType.NESTED_REPLY_ADD));
        }

        if (!boardWriter.getEmail().equals(replyWriter.getEmail())) {
            eventPublisher.publishEvent(new NotificationEvent(
                    boardWriter.getEmail(),
                    board.getId(),
                    reply.getId(),
                    NotificationMessage.REPLY_ADD.getMessage(),
                    NotificationType.REPLY_ADD));
        }
    }
}
