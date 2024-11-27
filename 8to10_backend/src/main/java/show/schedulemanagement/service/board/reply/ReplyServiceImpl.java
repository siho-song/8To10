package show.schedulemanagement.service.board.reply;

import static show.schedulemanagement.exception.ExceptionCode.INVALID_REPLY_LEVEL;
import static show.schedulemanagement.exception.ExceptionCode.NOT_EQUAL_BOARD;
import static show.schedulemanagement.exception.ExceptionCode.NOT_FOUND_REPLY;
import static show.schedulemanagement.exception.ExceptionCode.WRITER_NOT_EQUAL_MEMBER;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.reply.ReplySaveRequest;
import show.schedulemanagement.dto.board.reply.ReplyUpdateRequest;
import show.schedulemanagement.exception.InvalidLevelException;
import show.schedulemanagement.exception.MismatchException;
import show.schedulemanagement.exception.NotFoundEntityException;
import show.schedulemanagement.repository.board.reply.ReplyHeartRepository;
import show.schedulemanagement.repository.board.reply.ReplyRepository;
import show.schedulemanagement.service.board.BoardService;
import show.schedulemanagement.service.event.reply.ReplyEvent;
import show.schedulemanagement.service.notification.AsyncEventPublisher;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final BoardService boardService;
    private final ReplyHeartRepository replyHeartRepository;
    private final AsyncEventPublisher eventPublisher;

    @Override
    public Reply findById(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));
    }

    @Override
    public Reply findByIdWithParent(Long id) {
        return replyRepository.findByIdWithParent(id).orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));
    }

    @Override
    public Reply findByIdWithMemberAndParent(Long id) {
        return replyRepository.findByIdWithMemberAndParent(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_REPLY));
    }

    @Override
    public List<Reply> findAllByMemberWithBoard(Member member) {
        return replyRepository.findAllByMemberWithBoard(member);
    }

    @Override
    public List<Reply> findAllWithBoardAndMemberByEmail(Member member) {
        String email = member.getEmail();
        return replyRepository.findAllWithBoardAndMemberByEmail(email);
    }

    @Override
    public List<Reply> findNestedRepliesByParent(Reply reply) {
        return replyRepository.findNestedRepliesByParent(reply);
    }

    @Override
    public void deleteByReplies(List<Reply> replies) {
        replyRepository.deleteByReplies(replies);
    }

    @Override
    @Transactional
    public Reply save(ReplySaveRequest request, Member member) {
        Board board = boardService.findByIdWithMember(request.getBoardId());
        Long parentId = request.getParentId();

        Reply reply = Reply.from(null, request.getContents(), member, board);

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

    @Override
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

    @Override
    @Transactional
    public void update(Member member, ReplyUpdateRequest updateRequest) {
        Reply reply = findByIdWithMemberAndParent(updateRequest.getId());
        String createdBy = reply.getMember().getEmail();
        if(!member.isSameEmail(createdBy)){
            throw new RuntimeException();
        }
        reply.updateContents(updateRequest.getContents());
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
        throw new InvalidLevelException(INVALID_REPLY_LEVEL);
    }
}
