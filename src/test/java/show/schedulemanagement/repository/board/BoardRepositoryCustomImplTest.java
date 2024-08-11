package show.schedulemanagement.repository.board;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.dto.board.BoardPageResponse;
import show.schedulemanagement.dto.board.BoardPageRequest;
import show.schedulemanagement.dto.board.SearchCond;
import show.schedulemanagement.dto.board.SortCondition;

@SpringBootTest

class BoardRepositoryCustomImplTest {
    @Autowired
    BoardRepository boardRepository;

    @Test
    @Transactional
    @DisplayName("게시글 페이지 조회")
    void searchPage() {
        BoardPageRequest cond = new BoardPageRequest();
        cond.setSearchCond(SearchCond.WRITER);
        cond.setPageNum(0);
        cond.setPageSize(10);
        cond.setKeyword("Nick");
        cond.setSortCond(SortCondition.LIKE);
        Pageable pageable = PageRequest.of(cond.getPageNum(), cond.getPageSize());

        Page<BoardPageResponse> page = boardRepository.searchPage(cond, pageable);
        List<BoardPageResponse> content = page.getContent();
        for (BoardPageResponse responseDto : content) {
            System.out.println("responseDto = " + responseDto);
        }

        assertThat(content.size()).isEqualTo(10);
        assertThat(page.getTotalElements()).isEqualTo(45L);
        assertThat(page.getTotalPages()).isEqualTo(5);
    }

    @Test
    @DisplayName("게시글 단건 조회, 조회시 연관 댓글과 멤버도 함께 불러온다.")
    void search(){
        Long id = 1L;
        Optional<Board> byIdWithReplies = boardRepository.findByIdWithReplies(id);
        Board board = byIdWithReplies.orElse(null);
        assertThat(board).isNotNull();
    }
}