package show.schedulemanagement.service;


import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.dto.Result;
import show.schedulemanagement.dto.mypage.MemberBoardsResponse;
import show.schedulemanagement.dto.mypage.MemberRepliesResponse;
import show.schedulemanagement.dto.mypage.ProfileResponse;
import show.schedulemanagement.dto.mypage.ScrappedBoardResponse;
import show.schedulemanagement.service.board.BoardScrapService;
import show.schedulemanagement.service.board.BoardService;
import show.schedulemanagement.service.board.reply.ReplyService;

@SpringBootTest
@DisplayName("마이페이지 서비스 테스트")
class MyPageServiceTest {

    @MockBean
    BoardService boardService;

    @MockBean
    ReplyService replyService;

    @MockBean
    BoardScrapService boardScrapService;

    @Autowired
    MyPageService myPageService;

    @Autowired
    MemberService memberService;

    @Autowired
    MultipartFileStorageService multipartFileStorageService;

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

    @Test
    @DisplayName("유저가 스크랩한 게시글을 불러온다.")
    @WithUserDetails("normal@example.com")
    void getScrappedBoard() {
        //given
        Member member = memberService.getAuthenticatedMember();
        Board board1 = Board.builder().id(1L).build();
        Board board2 = Board.builder().id(2L).build();
        BoardScrap boardScrap1 = BoardScrap.builder().member(member).board(board1).build();
        BoardScrap boardScrap2 = BoardScrap.builder().member(member).board(board2).build();
        List<BoardScrap> boardScraps = List.of(boardScrap1, boardScrap2);

        when(boardScrapService.findAllByMemberWithBoard(member)).thenReturn(boardScraps);

        //when
        Result<ScrappedBoardResponse> result = myPageService.getScrappedBoard(member);
        List<ScrappedBoardResponse> items = result.getItems();

        //then
        assertThat(items).hasSize(boardScraps.size());
    }

    @Test
    @DisplayName("유저의 닉네임을 업데이트 한다.")
    @WithUserDetails("normal@example.com")
    void updateNickname() {
        //given
        Member member = memberService.getAuthenticatedMember();
        String beforeNickname = member.getNickname();
        String updateNickname = "업데이트된닉네임";

        //when
        myPageService.updateNickname(updateNickname, member.getId());

        //then
        Member updatedMember = memberService.findById(member.getId());
        assertThat(updatedMember.getNickname()).isEqualTo(updateNickname);
        myPageService.updateNickname(beforeNickname,member.getId());
    }

    @Test
    @DisplayName("유저의 비밀번호를 업데이트 한다.")
    @WithUserDetails("normal@example.com")
    void updatePassword() {
        //given
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Member member = memberService.getAuthenticatedMember();
        String beforePassword = member.getPassword();
        String updatePassword = "newPassword12!";

        //when
        myPageService.updatePassword(updatePassword, member.getId());

        //then
        Member updatedMember = memberService.findById(member.getId());
        assertThat(encoder.matches(updatePassword,updatedMember.getPassword())).isTrue();
        myPageService.updatePassword(beforePassword,member.getId());
    }

    @Test
    @DisplayName("유저의 프로필 사진을 등록,업데이트 한다.")
    @WithUserDetails("normal@example.com")
    void uploadProfilePhoto() throws IOException {
        //given
        Member member = memberService.getAuthenticatedMember();
        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "testFile.jpg",
                "image/jpg",
                "Test image content".getBytes());
        //when
        myPageService.uploadProfilePhoto(member, imageFile);

        //then
        Member findMember = memberService.findById(member.getId());
        assertThat(findMember.getImageFile()).isNotNull();
        multipartFileStorageService.deleteFile(findMember.getImageFile());
    }

    @Test
    @DisplayName("유저의 프로필 사진을 삭제 한다.")
    @WithUserDetails("normal@example.com")
    void deleteProfilePhoto() throws IOException {
        //given
        Member member = memberService.getAuthenticatedMember();
        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "testFile.jpg",
                "image/jpg",
                "Test image content".getBytes());

        myPageService.uploadProfilePhoto(member, imageFile);

        //when
        myPageService.deleteProfilePhoto(member);

        //then
        Member findMember = memberService.findById(member.getId());
        assertThat(findMember.getImageFile()).isNull();
    }
}