package com.eighttoten.community.service;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_REPLY;
import static com.eighttoten.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import com.eighttoten.community.domain.reply.NewReply;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.UpdateReply;
import com.eighttoten.community.domain.reply.ReplyWithPost;
import com.eighttoten.community.domain.reply.repository.ReplyHeartRepository;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.community.event.reply.ReplyAddEvent;
import com.eighttoten.exception.BadRequestException;
import com.eighttoten.exception.ExceptionCode;
import com.eighttoten.exception.MismatchException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.service.AsyncNotificationEventPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReplyService{
    private final ReplyRepository replyRepository;
    private final ReplyHeartRepository replyHeartRepository;
    private final AsyncNotificationEventPublisher eventPublisher;

    public Reply findById(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));
    }

    public ReplyWithPost findByIdWithPost(Long id) {
        return replyRepository.findByIdWithPost(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));
    }

    public List<Reply> findAllByMemberId(Long memberId) {
        return replyRepository.findAllByMemberId(memberId);
    }

    public List<Reply> findNestedRepliesByParentId(Long parentReplyId) {
        return replyRepository.findNestedRepliesByParentId(parentReplyId);
    }

    @Transactional
    public void add(NewReply newReply) {
        String parentReplyWriter = null;
        Long parentId = newReply.getParentId();

        if(parentId != null){
            ReplyWithPost parentReply = findByIdWithPost(parentId);
            if (parentReply.getPost().checkIsEqualId(newReply.getPostId()) && parentReply.getParentId() == null) {
                parentReplyWriter = parentReply.getCreatedBy();
            } else {
                throw new BadRequestException(ExceptionCode.INVALID_REPLY_SAVE);
            }
        }

        long savedId = replyRepository.save(newReply);
        eventPublisher.notifyReplyAddEvent(ReplyAddEvent.from(findByIdWithPost(savedId), parentReplyWriter));
    }

    @Transactional
    public void update(Member member, UpdateReply updateReply) {
        Reply reply = replyRepository.findById(updateReply.getId())
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));

        if(!member.isSameEmail(reply.getCreatedBy())){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }
        reply.update(updateReply);
        replyRepository.update(reply);
    }

    @Transactional
    public void deleteById(Member member, Long id) {
        Reply reply = replyRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));

        String createdBy = reply.getCreatedBy();

        if(!member.isSameEmail(createdBy)){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }

        if (reply.getParentId() != null) { //자식 댓글
            replyHeartRepository.deleteByReplyId(id);
            replyRepository.deleteById(id);
            return;
        }

        //부모 댓글
        List<Long> nestedReplyIds = findNestedRepliesByParentId(reply.getId())
                .stream().map(Reply::getId).toList();

        replyHeartRepository.deleteByReplyIds(nestedReplyIds);
        replyRepository.deleteByReplyIds(nestedReplyIds);
        replyRepository.deleteById(id);
    }
}