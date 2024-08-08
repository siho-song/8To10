package show.schedulemanagement.repository.board;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.dto.board.BoardResponseDto;
import show.schedulemanagement.dto.board.BoardSearchCond;
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
        BoardSearchCond cond = new BoardSearchCond();
        cond.setSearchCond(SearchCond.WRITER);
        cond.setPageNum(0);
        cond.setPageSize(10);
        cond.setKeyword("Nick");
        cond.setSortCond(SortCondition.LIKE);
        Pageable pageable = PageRequest.of(cond.getPageNum(), cond.getPageSize());

        Page<BoardResponseDto> page = boardRepository.searchPage(cond, pageable);
        List<BoardResponseDto> content = page.getContent();
        for (BoardResponseDto responseDto : content) {
            System.out.println("responseDto = " + responseDto);
        }

        assertThat(content.size()).isEqualTo(10);
        assertThat(page.getTotalElements()).isEqualTo(45L);
        assertThat(page.getTotalPages()).isEqualTo(5);
    }
}