package show.schedulemanagement.service.board;

import static show.schedulemanagement.exception.ExceptionCode.NOT_FOUND_BOARD;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.BoardPageRequest;
import show.schedulemanagement.dto.board.BoardPageResponse;
import show.schedulemanagement.dto.board.BoardSearchResponse;
import show.schedulemanagement.dto.board.BoardUpdateRequest;
import show.schedulemanagement.exception.NotFoundEntityException;
import show.schedulemanagement.repository.board.BoardHeartRepository;
import show.schedulemanagement.repository.board.BoardRepository;
import show.schedulemanagement.repository.board.BoardScrapRepository;
import show.schedulemanagement.repository.board.reply.ReplyHeartRepository;
import show.schedulemanagement.service.event.board.BoardEvent;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardHeartRepository boardHeartRepository;
    private final BoardScrapRepository boardScrapRepository;
    private final ReplyHeartRepository replyHeartRepository;

    @Override
    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    @Override
    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD));
    }

    @Override
    public Board findByIdWithMember(Long id) {
        return boardRepository.findByIdWithMember(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD));
    }

    @Override
    public Board findByIdWithRepliesAndMember(Long id) {
        return boardRepository.findByIdWithRepliesAndMember(id)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD));
    }

    @Override
    public Page<BoardPageResponse> searchBoardPage(BoardPageRequest searchCond, Pageable pageable) {
        return boardRepository.searchPage(searchCond, pageable);
    }

    @Override
    public BoardSearchResponse searchBoard(Long id, Member member) {
        return boardRepository.searchBoard(id, member)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_BOARD));
    }

    @Override
    @Transactional
    public void deleteById(Member member, Long id) {
        Board board = findByIdWithRepliesAndMember(id);
        String createdBy = board.getMember().getEmail();
        if(member.isSameEmail(createdBy)){
            boardHeartRepository.deleteHeartsByBoardId(id);
            boardScrapRepository.deleteScrapByBoardId(id);
            replyHeartRepository.deleteByReplies(board.getReplies());
            boardRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    public void update(Member member, BoardUpdateRequest updateRequest) {
        Board board = findByIdWithMember(updateRequest.getId());
        String createdBy = board.getMember().getEmail();
        if(member.isSameEmail(createdBy)){
            board.update(updateRequest.getTitle(), updateRequest.getContents());
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void handleEvent(BoardEvent event){
        Board board = findById(event.getBoardId());
        event.execute(board);
    }
}
