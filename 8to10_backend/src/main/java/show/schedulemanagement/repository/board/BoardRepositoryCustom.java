package show.schedulemanagement.repository.board;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.board.BoardPageRequest;
import show.schedulemanagement.dto.board.BoardPageResponse;
import show.schedulemanagement.dto.board.BoardSearchResponse;

public interface BoardRepositoryCustom {
    Page<BoardPageResponse> searchPage(BoardPageRequest cond, Pageable pageable);
    Optional<BoardSearchResponse> searchBoard(Long id, Member member);
}
