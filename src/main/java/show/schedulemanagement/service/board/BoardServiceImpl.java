package show.schedulemanagement.service.board;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.dto.board.BoardResponseDto;
import show.schedulemanagement.dto.board.BoardSearchCond;
import show.schedulemanagement.repository.board.BoardRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;

    @Override
    @Transactional
    public void save(Board board) {
        boardRepository.save(board);
    }

    @Override
    public Page<BoardResponseDto> searchBoardPage(BoardSearchCond searchCond, Pageable pageable) {
        return boardRepository.searchPage(searchCond, pageable);
    }
}
