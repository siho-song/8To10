package com.eighttoten.community.service;

import static com.eighttoten.global.exception.ExceptionCode.INVALID_REPLY_LEVEL;
import static com.eighttoten.global.exception.ExceptionCode.NOT_EQUAL_BOARD;
import static com.eighttoten.global.exception.ExceptionCode.NOT_FOUND_REPLY;
import static com.eighttoten.global.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import com.eighttoten.community.domain.Board;
import com.eighttoten.community.domain.Reply;
import com.eighttoten.community.domain.repository.ReplyHeartRepository;
import com.eighttoten.community.domain.repository.ReplyRepository;
import com.eighttoten.community.dto.ReplySaveRequest;
import com.eighttoten.community.dto.ReplyUpdateRequest;
import com.eighttoten.community.dto.event.ReplyEvent;
import com.eighttoten.global.exception.BadRequestException;
import com.eighttoten.global.exception.MismatchException;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.notification.service.AsyncNotificationEventPublisher;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReplyService{
    private final ReplyRepository replyRepository;
    private final BoardService boardService;
    private final ReplyHeartRepository replyHeartRepository;
    private final AsyncNotificationEventPublisher eventPublisher;

    public Reply findById(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));
    }

    public Reply findByIdWithParent(Long id) {
        return replyRepository.findByIdWithParent(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));
    }

    public Reply findByIdWithMemberAndParent(Long id) {
        return replyRepository.findByIdWithMemberAndParent(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));
    }

    public List<Reply> findAllByMemberWithBoard(Member member) {
        return replyRepository.findAllByMemberWithBoard(member);
    }

    public List<Reply> findAllWithBoardAndMemberByEmail(Member member) {
        String email = member.getEmail();
        return replyRepository.findAllWithBoardAndMemberByEmail(email);
    }

    public List<Reply> findNestedRepliesByParent(Reply reply) {
        return replyRepository.findNestedRepliesByParent(reply);
    }

    @Transactional
    public void update(Member member, ReplyUpdateRequest updateRequest) {
        Reply reply = findByIdWithMemberAndParent(updateRequest.getId());
        String createdBy = reply.getMember().getEmail();
        if(!member.isSameEmail(createdBy)){
            throw new RuntimeException();
        }
        reply.updateContents(updateRequest.getContents());
    }

    @Transactional
    public void delete(Member member, Long id) {
        Reply reply = findByIdWithMemberAndParent(id);
        String createdBy = reply.getMember().getEmail();

        if(!member.isSameEmail(createdBy)){
            throw new MismatchException(WRITER_NOT_EQUAL_MEMBER);
        }

        if (reply.getParent() != null) {
            replyHeartRepository.deleteByReplyId(id);
            replyRepository.delete(reply);
            return;
        }

        List<Reply> nestedReplies = findNestedRepliesByParent(reply);
        replyHeartRepository.deleteByReplies(nestedReplies);
        deleteByReplies(nestedReplies);
        replyHeartRepository.deleteByReplyId(id);
        replyRepository.delete(reply);
    }

    @Transactional
    public void deleteByReplies(List<Reply> replies) {
        replyRepository.deleteByReplies(replies);
    }

    @Transactional
    public Reply save(ReplySaveRequest request, Member member) {
        Board board = boardService.findByIdWithMember(request.getBoardId());
        Long parentId = request.getParentId();

        Reply reply = Reply.of(null, request.getContents(), member, board);

        if(parentId != null){
            Reply parent = findByIdWithParent(parentId);
            if(checkEqualBoard(parent.getBoard(), board) && checkLevel(parent)){
                reply.assignParent(parent);
            }
        }
        replyRepository.save(reply);
        eventPublisher.publishReplyAddEvent(board, reply);
        return reply;
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEvent(ReplyEvent event){
        Reply reply = findById(event.getReplyId());
        event.execute(reply);
    }

    private boolean checkEqualBoard(Board parentReplyBoard, Board board) {
        if(parentReplyBoard.getId().equals(board.getId())){
            return true;
        }
        throw new MismatchException(NOT_EQUAL_BOARD);
    }

    private boolean checkLevel(Reply parent) {
        if (parent.getParent() == null){
            return true;
        }
        throw new BadRequestException(INVALID_REPLY_LEVEL);
    }
}