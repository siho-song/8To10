package show.schedulemanagement.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@DisplayName("게시글 좋아요 서비스 테스트")
@Slf4j
class BoardHeartServiceTest {

    @Autowired
    BoardHeartService boardHeartService;

    @Autowired
    BoardService boardService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("게시글 좋아요 정상 등록")
    @WithUserDetails(value = "faithful@example.com")
    void add(){
        Member member = memberService.getAuthenticatedMember();
        Long boardId = 1L;
        boardHeartService.add(boardId, member); //정상 작동

        Board updatedBoard = boardService.findById(boardId);
        assertThat(updatedBoard.getTotalLike()).isEqualTo(6);
        boardHeartService.delete(boardId,member);
    }

    @Test
    @DisplayName("게시글 좋아요 등록 - 이미 좋아요한 게시글인 경우 예외 발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void add_liked(){
        Member member = memberService.getAuthenticatedMember();
        Long boardId = 1L;

        assertThatThrownBy(() -> boardHeartService.add(boardId, member)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("게시글 좋아요 삭제")
    @WithUserDetails(value = "normal@example.com")
    void delete(){
        Long boardId = 1L;
        Member member = memberService.getAuthenticatedMember();

        boardHeartService.delete(boardId, member);
        Board updatedBoard = boardService.findById(boardId);

        assertThat(updatedBoard.getTotalLike()).isEqualTo(4);

        boardHeartService.add(boardId,member); // 데이터 복구
    }

    @Test
    @DisplayName("게시글 좋아요 삭제 - 삭제할 좋아요가 없는 경우 예외발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void delete_not_exist(){
        Long boardId = 3L;
        Member member = memberService.getAuthenticatedMember();

        assertThatThrownBy(() -> boardHeartService.delete(boardId, member)).isInstanceOf(
                EntityNotFoundException.class);
    }
}