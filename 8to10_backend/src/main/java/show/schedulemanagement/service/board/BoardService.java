package show.schedulemanagement.service.board;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.BoardPageRequest;
import show.schedulemanagement.dto.board.BoardPageResponse;
import show.schedulemanagement.dto.board.BoardSearchResponse;
import show.schedulemanagement.dto.board.BoardUpdateRequest;

public interface BoardService {
    void save(Board board);
    Board findById(Long id);
    Board findByIdWithMember(Long id);
    Board findByIdWithRepliesAndMember(Long id);
    List<Board> findAllByMember(Member member);
    Page<BoardPageResponse> searchBoardPage(BoardPageRequest searchCond, Pageable pageable);
    BoardSearchResponse searchBoard(Long id, Member member);
    void deleteById(Member member, Long id);
    void update(Member member, BoardUpdateRequest updateRequest);
}
