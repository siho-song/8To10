package show.schedulemanagement.service.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.BoardPageResponse;
import show.schedulemanagement.dto.board.BoardPageRequest;

public interface BoardService {
    void save(Board board);
    Board findByIdWithReplies(Long id);
    Page<BoardPageResponse> searchBoardPage(BoardPageRequest searchCond, Pageable pageable);
    void deleteById(Member member, Long id);
}
