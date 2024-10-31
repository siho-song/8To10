package show.schedulemanagement.service.board.reply;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.reply.ReplySaveRequest;
import show.schedulemanagement.dto.board.reply.ReplyUpdateRequest;
import show.schedulemanagement.repository.board.reply.ReplyRepository;
import show.schedulemanagement.service.board.BoardService;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final BoardService boardService;
    private final ReplyHeartService replyHeartService;

    @Override
    public Reply findById(Long id) {
        return replyRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
    }

    @Override
    public Reply findByIdWithParent(Long id) {
        return replyRepository.findByIdWithParent(id).orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
    }

    @Override
    public Reply findByIdWithMemberAndParent(Long id) {
        return replyRepository.findByIdWithMemberAndParent(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
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
        Board board = boardService.findById(request.getBoardId());
        Long parentId = request.getParentId();

        if(parentId != null){
            Reply parent = findByIdWithParent(parentId);
            if(checkEqualBoard(parent.getBoard(), board) && checkLevel(parent)){
                Reply child = Reply.from(parent, request.getContents(), member, board);
                replyRepository.save(child);
                return child;
            }
        }
        Reply reply = Reply.from(null, request.getContents(), member, board);
        replyRepository.save(reply);
        return reply;
    }

    @Override
    @Transactional
    public void delete(Member member, Long id) {
        Reply reply = findByIdWithMemberAndParent(id);
        String createdBy = reply.getMember().getEmail();

        if(!member.isSameEmail(createdBy)){
            throw new RuntimeException();
        }
            // 댓글 삭제
        if (reply.getParent() != null) { // 대댓글 삭제
            replyHeartService.deleteByReply(reply);
            replyRepository.delete(reply);
            return;
        }
        List<Reply> nestedReplies = findNestedRepliesByParent(reply);
            replyHeartService.deleteByReplies(nestedReplies);
            deleteByReplies(nestedReplies);
            replyHeartService.deleteByReply(reply);
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

    private boolean checkEqualBoard(Board parentReplyBoard, Board board) {
        if(parentReplyBoard.getId().equals(board.getId())){
            return true;
        }
        throw new RuntimeException("부모댓글의 게시글과 대댓글의 게시글이 일치하지 않습니다."); //TODO Exception 처리
    }

    private boolean checkLevel(Reply parent) {
        if (parent.getParent() == null){
            return true;
        }
        throw new RuntimeException("대댓글에는 댓글을 달 수 없습니다."); //TODO Exception 처리
    }
}
