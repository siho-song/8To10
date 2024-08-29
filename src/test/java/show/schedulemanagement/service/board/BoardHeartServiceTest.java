package show.schedulemanagement.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@Transactional
@DisplayName("게시글 좋아요 서비스 테스트")
class BoardHeartServiceTest {

    @Autowired
    BoardHeartService boardHeartService;

    @Autowired
    BoardService boardService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    void setAuthentication(){
        MemberDetailsDto user = new MemberDetailsDto(memberService.loadUserByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("게시글 좋아요 정상 등록")
    void add(){
        Member member = memberService.findByEmail("faithful@example.com");
        Long boardId = 1L;
        Board board = boardService.findById(boardId);

        boardHeartService.add(board, member);

        assertThat(board.getTotalLike()).isEqualTo(6);
    }

    @Test
    @DisplayName("게시글 좋아요 등록 - 이미 좋아요한 게시글인 경우 예외 발생")
    void add_liked(){
        Member member = memberService.getAuthenticatedMember();
        Long boardId = 1L;
        Board board = boardService.findById(boardId);

        assertThatThrownBy(() -> boardHeartService.add(board, member)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("게시글 좋아요 삭제")
    void delete(){
        Long boardId = 1L;
        Member member = memberService.getAuthenticatedMember();
        Board board = boardService.findById(boardId);

        boardHeartService.delete(board, member);

        assertThat(board.getTotalLike()).isEqualTo(4);
    }

    @Test
    @DisplayName("게시글 좋아요 삭제 - 삭제할 좋아요가 없는 경우 예외발생")
    void delete_not_exist(){
        Long boardId = 3L;
        Board board = boardService.findById(boardId);
        Member member = memberService.getAuthenticatedMember();

        assertThatThrownBy(() -> boardHeartService.delete(board, member)).isInstanceOf(
                EntityNotFoundException.class);
    }
}