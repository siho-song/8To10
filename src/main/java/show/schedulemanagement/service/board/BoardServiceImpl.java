package show.schedulemanagement.service.board;

import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.BoardPageResponse;
import show.schedulemanagement.dto.board.BoardPageRequest;
import show.schedulemanagement.dto.board.BoardSearchResponse;
import show.schedulemanagement.dto.board.BoardUpdateRequest;
import show.schedulemanagement.repository.board.BoardRepository;
import show.schedulemanagement.repository.board.BoardScrapRepository;
import show.schedulemanagement.service.board.reply.ReplyHeartService;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardHeartService boardHeartService;
    private final BoardScrapRepository boardScrapRepository;
    private final ReplyHeartService replyHeartService;

    @Override
    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    @Override
    public Board findById(Long id) {
        return boardRepository.findById(id).orElseThrow(()->new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    @Override
    public Board findByIdWithMember(Long id) {
        return boardRepository.findByIdWithMember(id).orElseThrow(()->new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    @Override
    public Board findByIdWithRepliesAndMember(Long id) {
        Optional<Board> board = boardRepository.findByIdWithRepliesAndMember(id);
        return board.orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    @Override
    public Page<BoardPageResponse> searchBoardPage(BoardPageRequest searchCond, Pageable pageable) {
        return boardRepository.searchPage(searchCond, pageable);
    }

    @Override
    public BoardSearchResponse searchBoard(Long id, Member member) {
        return boardRepository.searchBoard(id,member).orElseThrow(()-> new EntityNotFoundException("해당 게시물을 찾을 수 없습니다."));
    }

    @Override
    @Transactional
    public void deleteById(Member member, Long id) {
        Board board = findByIdWithRepliesAndMember(id);
        String createdBy = board.getMember().getEmail();
        if(member.isSameEmail(createdBy)){
            boardHeartService.deleteHeartsByBoard(board);
            boardScrapRepository.deleteScrapByBoard(board);
            replyHeartService.deleteByReplies(board.getReplies());
            boardRepository.delete(board);
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
}
