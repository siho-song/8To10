package com.eighttoten.community.service;

import static com.eighttoten.exception.ExceptionCode.DUPLICATED_REPLY_HEART;

import com.eighttoten.community.domain.reply.NewReplyHeart;
import com.eighttoten.community.domain.reply.repository.ReplyHeartRepository;
import com.eighttoten.community.event.reply.ReplyHeartAddEvent;
import com.eighttoten.community.event.reply.ReplyHeartSubEvent;
import com.eighttoten.exception.DuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyHeartService {
    private final ReplyHeartRepository replyHeartRepository;
    private final ApplicationEventPublisher publisher;

    @Transactional
    public void add(NewReplyHeart newReplyHeart) {
        Long memberId = newReplyHeart.getMemberId();
        Long replyId = newReplyHeart.getReplyId();
        boolean hasLiked = replyHeartRepository.existsByMemberIdAndReplyId(memberId, replyId);

        if (hasLiked) {
            throw new DuplicatedException(DUPLICATED_REPLY_HEART);
        }

        replyHeartRepository.save(newReplyHeart);
        publisher.publishEvent(new ReplyHeartAddEvent(replyId));
    }

    @Transactional
    public void deleteByMemberIdAndReplyId(Long memberId, Long replyId) {
        replyHeartRepository.deleteByMemberIdAndReplyId(memberId, replyId);
        publisher.publishEvent(new ReplyHeartSubEvent(replyId));
    }
}