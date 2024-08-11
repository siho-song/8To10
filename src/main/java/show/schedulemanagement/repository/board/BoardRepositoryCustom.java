package show.schedulemanagement.repository.board;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import show.schedulemanagement.dto.board.BoardPageResponse;
import show.schedulemanagement.dto.board.BoardPageRequest;

public interface BoardRepositoryCustom {
    Page<BoardPageResponse> searchPage(BoardPageRequest cond, Pageable pageable);
}
