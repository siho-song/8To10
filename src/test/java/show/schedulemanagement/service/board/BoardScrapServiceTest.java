package show.schedulemanagement.service.board;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@Transactional
@DisplayName("게시글 스크랩 서비스")
class BoardScrapServiceTest {

    @Autowired
    BoardScrapService boardScrapService;

    @Autowired
    MemberService memberService;

    @Autowired
    BoardService boardService;

    Member normalMember;

    @BeforeEach
    void init(){
        normalMember = memberService.findByEmail("normal@example.com");
    }

    @Test
    @DisplayName("게시글 스크랩 정상 등록")
    void add() {
        Board board = boardService.findById(2L);
        boardScrapService.add(normalMember,board);
        assertThat(board.getTotalScrap()).isEqualTo(2);
    }

    @Test
    @DisplayName("게시글 스크랩 등록 실패 - 이미 스크랩한 게시글 예외 발생")
    void add_scraped(){
        Board board = boardService.findById(1L);
        assertThatThrownBy(() -> boardScrapService.add(normalMember, board)).isInstanceOf(RuntimeException.class);
    }
}