package com.eighttoten.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import com.eighttoten.domain.board.Board;
import com.eighttoten.domain.board.BoardScrap;
import com.eighttoten.domain.board.reply.Reply;
import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.Result;
import com.eighttoten.dto.mypage.MemberBoardsResponse;
import com.eighttoten.dto.mypage.MemberRepliesResponse;
import com.eighttoten.dto.mypage.ProfileResponse;
import com.eighttoten.dto.mypage.ScrappedBoardResponse;
import com.eighttoten.service.board.BoardScrapService;
import com.eighttoten.service.board.BoardService;
import com.eighttoten.service.board.reply.ReplyService;
import com.eighttoten.service.member.MemberService;

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
        Board board1 = TestDataUtils.createTestBoard(member);
        Board board2 = TestDataUtils.createTestBoard(member);
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
        Board board1 = TestDataUtils.createTestBoard(member);
        Board board2 = TestDataUtils.createTestBoard(member);
        Reply reply1 = TestDataUtils.createTestReply(null, member, board1);
        Reply reply2 = TestDataUtils.createTestReply(null, member, board2);
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
        Board board1 = TestDataUtils.createTestBoard(member);
        Board board2 = TestDataUtils.createTestBoard(member);
        BoardScrap boardScrap1 = TestDataUtils.createTestBoardScrap(board1,member);
        BoardScrap boardScrap2 = TestDataUtils.createTestBoardScrap(board2,member);
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