package com.eighttoten.community.service;

import static com.eighttoten.global.exception.ExceptionCode.DUPLICATED_REPLY_HEART;
import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_REPLY_HEART;

import com.eighttoten.community.domain.Reply;
import com.eighttoten.community.domain.ReplyHeart;
import com.eighttoten.community.domain.repository.ReplyHeartRepository;
import com.eighttoten.community.dto.event.ReplyHeartAddEvent;
import com.eighttoten.community.dto.event.ReplyHeartSubEvent;
import com.eighttoten.global.exception.DuplicatedException;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ReplyHeartService {
    private final ReplyHeartRepository replyHeartRepository;
    private final ReplyService replyService;
    private final ApplicationEventPublisher publisher;

    public boolean existsReplyHeartByMemberAndReplyId(Member member, Long replyId) {
        return replyHeartRepository.existsReplyHeartByMemberAndReplyId(member, replyId);
    }

    public ReplyHeart findByMemberAndReplyId(Member member, Long replyId) {
        return replyHeartRepository.findByMemberAndReplyId(member, replyId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY_HEART));
    }

    @Transactional
    public void add(Long replyId, Member member) {
        boolean hasLiked = existsReplyHeartByMemberAndReplyId(member, replyId);

        if(hasLiked){
            throw new DuplicatedException(DUPLICATED_REPLY_HEART);
        }
        Reply reply = replyService.findById(replyId);
        ReplyHeart replyHeart = ReplyHeart.of(reply, member);

        replyHeartRepository.save(replyHeart);
        publisher.publishEvent(new ReplyHeartAddEvent(replyId));
    }

    @Transactional
    public void delete(Long replyId, Member member) {
        ReplyHeart replyHeart = findByMemberAndReplyId(member, replyId);

        replyHeartRepository.delete(replyHeart);
        publisher.publishEvent(new ReplyHeartSubEvent(replyId));
    }
}