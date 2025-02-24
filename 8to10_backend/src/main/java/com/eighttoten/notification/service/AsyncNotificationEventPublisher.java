package com.eighttoten.notification.service;

import com.eighttoten.achievement.domain.Achievement;
import com.eighttoten.achievement.service.AchievementService;
import com.eighttoten.community.domain.Board;
import com.eighttoten.community.domain.Reply;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.service.MemberService;
import com.eighttoten.notification.domain.FeedbackMessage;
import com.eighttoten.notification.domain.NotificationMessage;
import com.eighttoten.notification.domain.NotificationType;
import com.eighttoten.notification.event.NotificationEvent;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AsyncNotificationEventPublisher {
    private final ApplicationEventPublisher eventPublisher;
    private final AchievementService achievementService;
    private final MemberService memberService;

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

    @Scheduled(cron = "1 0 0 * * *")
    public void publishFeedBackEvent(){
        List<Achievement> achievements = achievementService.findAllByDateWithMember(LocalDate.now().minusDays(1L));
        for (Achievement achievement : achievements) {
            Member member = achievement.getMember();
            FeedbackMessage feedbackMessage = FeedbackMessage.selectRandomMessage(
                    member.getMode(),
                    achievement.getAchievementRate()
            );

            NotificationType type = NotificationType.ACHIEVEMENT_FEEDBACK;
            eventPublisher.publishEvent(new NotificationEvent(
                    member.getEmail(),
                    null,
                    null,
                    feedbackMessage.getMessage(),
                    type));
        }
    }

    @Scheduled(cron = "0 0 22 * * *")
    public void publishTodoUpdateEvent(){
        List<Member> members = memberService.findAll();
        NotificationType type = NotificationType.TODO_UPDATE;
        for (Member member : members) {
            eventPublisher.publishEvent(new NotificationEvent(
                    member.getEmail(),
                    null,
                    null,
                    NotificationMessage.TODO_UPDATE.getMessage(),
                    type));
        }
    }
}