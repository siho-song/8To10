package show.schedulemanagement.service.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.BoardResponseDto;
import show.schedulemanagement.dto.board.BoardSearchCond;

public interface BoardService {
    void save(Board board);
    Board findByIdWithReplies(Long id);
    Page<BoardResponseDto> searchBoardPage(BoardSearchCond searchCond, Pageable pageable);
    void deleteById(Member member, Long id);
}
