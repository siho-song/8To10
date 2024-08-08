package show.schedulemanagement.service.board;

import jakarta.persistence.EntityManager;
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
import show.schedulemanagement.dto.board.BoardResponseDto;
import show.schedulemanagement.dto.board.BoardSearchCond;
import show.schedulemanagement.repository.board.BoardHeartsRepository;
import show.schedulemanagement.repository.board.BoardRepository;
import show.schedulemanagement.repository.board.BoardScrapRepository;
import show.schedulemanagement.repository.board.reply.ReplyHeartsRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardHeartsRepository boardHeartsRepository;
    private final BoardScrapRepository boardScrapRepository;
    private final ReplyHeartsRepository replyHeartsRepository;

    @Override
    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    @Override
    public Board findByIdWithReplies(Long id) {
        Optional<Board> board = boardRepository.findByIdWithReplies(id);
        return board.orElseThrow(() -> new EntityNotFoundException("해당 게시글을 찾을 수 없습니다."));
    }

    @Override
    public Page<BoardResponseDto> searchBoardPage(BoardSearchCond searchCond, Pageable pageable) {
        return boardRepository.searchPage(searchCond, pageable);
    }

    @Override
    @Transactional
    public void deleteById(Member member, Long id) {
        Board board = findByIdWithReplies(id);
        String createdBy = board.getMember().getEmail();
        if(member.getEmail().equals(createdBy)){
            boardHeartsRepository.deleteHeartsByBoardId(board);
            boardScrapRepository.deleteScrapByBoard(board);
            replyHeartsRepository.deleteByReplies(board.getReplies());
            boardRepository.delete(board);
        }
    }
}
