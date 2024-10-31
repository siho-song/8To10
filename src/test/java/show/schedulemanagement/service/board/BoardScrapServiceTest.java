package show.schedulemanagement.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@DisplayName("게시글 스크랩 서비스")
class BoardScrapServiceTest {

    @Autowired
    BoardScrapService boardScrapService;

    @Autowired
    MemberService memberService;

    @Autowired
    BoardService boardService;

    @Test
    @DisplayName("게시글 스크랩 정상 등록")
    @WithUserDetails(value = "normal@example.com")
    void add() {
        Member member = memberService.getAuthenticatedMember();
        Long boardId = 2L;
        boardScrapService.add(member,boardId);

        Board board = boardService.findById(boardId);
        assertThat(board.getTotalScrap()).isEqualTo(2);

        boardScrapService.delete(member, boardId);
    }

    @Test
    @DisplayName("게시글 스크랩 등록 실패 - 이미 스크랩한 게시글 예외 발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void add_scraped(){
        Member member = memberService.getAuthenticatedMember();
        Long boardId = 1L;

        assertThatThrownBy(() -> boardScrapService.add(member, boardId)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("게시글 스크랩 정상 삭제")
    @WithUserDetails(value = "normal@example.com")
    void delete(){
        Member member = memberService.getAuthenticatedMember();

        Long boardId = 1L;
        boardScrapService.delete(member, boardId);

        Board board = boardService.findById(boardId);
        assertThat(board.getTotalScrap()).isEqualTo(1L);

        boardScrapService.add(member,boardId);
    }

    @Test
    @DisplayName("게시글 스크랩 삭제 실패 - 삭제할 스크랩이 없는 경우 예외발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void delete_not_exist(){
        Member member = memberService.getAuthenticatedMember();

        Long boardId = 2L;
        assertThatThrownBy(() -> boardScrapService.delete(member, boardId)).isInstanceOf(
                EntityNotFoundException.class);
    }
}