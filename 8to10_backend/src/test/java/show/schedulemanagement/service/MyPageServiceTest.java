package show.schedulemanagement.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithUserDetails;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.mypage.MemberBoardsResponse;
import show.schedulemanagement.dto.mypage.MemberRepliesResponse;
import show.schedulemanagement.dto.mypage.ProfileResponse;
import show.schedulemanagement.service.board.BoardService;
import show.schedulemanagement.service.board.reply.ReplyService;

@SpringBootTest
@DisplayName("마이페이지 서비스 테스트")
class MyPageServiceTest {

    @MockBean
    BoardService boardService;

    @MockBean
    ReplyService replyService;

    @Autowired
    MyPageService myPageService;

    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("유저의 프로필을 불러온다.")
    @WithUserDetails("normal@example.com")
    void getProfile() {
        //given
        Member member = memberService.getAuthenticatedMember();

        //when
        ProfileResponse profile = myPageService.getProfile(member);

        //then
        assertThat(profile.getEmail()).isEqualTo("normal@example.com");
        assertThat(profile.getNickname()).isNotNull();
        assertThat(profile.getRole()).isNotNull();
    }

    @Test
    @DisplayName("유저가 작성한 게시글을 불러온다.")
    @WithUserDetails("normal@example.com")
    void getMemberBoards() {
        //given
        Member member = memberService.getAuthenticatedMember();
        Board board1 = Board.builder().member(member).title("test1").build();
        Board board2 = Board.builder().member(member).title("test2").build();
        List<Board> boards = List.of(board1, board2);

        when(boardService.findAllByMember(member)).thenReturn(boards);

        //when
        Result<MemberBoardsResponse> result = myPageService.getMemberBoards(member);
        List<MemberBoardsResponse> items = result.getItems();

        //then
        assertThat(items).hasSize(boards.size());
    }

    @Test
    @DisplayName("유저가 작성한 댓글을 불러온다.")
    @WithUserDetails("normal@example.com")
    void getMemberReplies() {
        //given
        Member member = memberService.getAuthenticatedMember();
        Board board1 = Board.builder().id(1L).build();
        Board board2 = Board.builder().id(2L).build();
        Reply reply1 = Reply.builder().member(member).board(board1).contents("test1").build();
        Reply reply2 = Reply.builder().member(member).board(board2).contents("test2").build();
        List<Reply> replies = List.of(reply1, reply2);

        when(replyService.findAllByMemberWithBoard(member)).thenReturn(replies);

        //when
        Result<MemberRepliesResponse> result = myPageService.getMemberReplies(member);
        List<MemberRepliesResponse> items = result.getItems();

        //then
        assertThat(items).hasSize(replies.size());
    }
}