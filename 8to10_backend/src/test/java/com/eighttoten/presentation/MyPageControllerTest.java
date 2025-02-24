package com.eighttoten.presentation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eighttoten.TestDataUtils;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.community.domain.Board;
import com.eighttoten.community.domain.BoardScrap;
import com.eighttoten.community.domain.Reply;
import com.eighttoten.member.domain.Member;
import com.eighttoten.infrastructure.TokenProvider;
import com.eighttoten.member.service.MemberService;
import com.eighttoten.community.service.BoardScrapService;
import com.eighttoten.community.service.BoardService;
import com.eighttoten.community.service.ReplyService;

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("마이페이지 컨트롤러 테스트")
class MyPageControllerTest {

    @MockBean
    BoardService boardService;

    @MockBean
    ReplyService replyService;

    @MockBean
    BoardScrapService boardScrapService;

    @MockBean
    MemberService memberService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    String token;

    @BeforeEach
    void init() {
        token = tokenProvider.generateAccessToken("normal@example.com");
        when(memberService.findById(any())).thenReturn(TestDataUtils.createTestMember());

    }

    @Test
    @DisplayName("유저의 프로필을 불러온다.")
    void getProfile() throws Exception {

        ResultActions resultActions = mockMvc.perform(get("/mypage")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        );

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.role").isNotEmpty());
    }

    @Test
    @DisplayName("유저가 작성한 게시글을 불러온다.")
    void getMemberBoards() throws Exception {
        //given
        Board board1 = TestDataUtils.createTestBoard(memberService.findById(1L));
        Board board2 = TestDataUtils.createTestBoard(memberService.findById(1L));
        Board board3 = TestDataUtils.createTestBoard(memberService.findById(1L));

        when(boardService.findAllByMember(any())).thenReturn(List.of(board1, board2, board3));

        //when
        ResultActions resultActions = mockMvc.perform(get("/mypage/boards")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3));
    }

    @Test
    @DisplayName("유저가 작성한 댓글들을 불러온다.")
    void getMemberReplies() throws Exception {
        //given
        Board board1 = TestDataUtils.createTestBoard(memberService.findById(1L));
        Board board2 = TestDataUtils.createTestBoard(memberService.findById(1L));
        Board board3 = TestDataUtils.createTestBoard(memberService.findById(1L));

        Reply reply1 = TestDataUtils.createTestReply(null,memberService.findById(1L),board1);
        Reply reply2 = TestDataUtils.createTestReply(null,memberService.findById(1L),board2);
        Reply reply3 = TestDataUtils.createTestReply(null,memberService.findById(1L),board3);

        when(replyService.findAllByMemberWithBoard(any())).thenReturn(List.of(reply1, reply2, reply3));

        //when
        ResultActions resultActions = mockMvc.perform(get("/mypage/replies")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3));
    }

    @Test
    @DisplayName("유저가 스크랩한 게시글들을 불러온다.")
    void getScrappedBoards() throws Exception {
        //given
        Board board1 = TestDataUtils.createTestBoard(memberService.findById(1L));
        Board board2 = TestDataUtils.createTestBoard(memberService.findById(1L));
        Board board3 = TestDataUtils.createTestBoard(memberService.findById(1L));

        BoardScrap boardScrap1 = BoardScrap.of(board1, memberService.findById(1L));
        BoardScrap boardScrap2 = BoardScrap.of(board2, memberService.findById(1L));
        BoardScrap boardScrap3 = BoardScrap.of(board3, memberService.findById(1L));

        when(boardScrapService.findAllByMemberWithBoard(any())).thenReturn(
                List.of(boardScrap1, boardScrap2, boardScrap3));

        //when
        ResultActions resultActions = mockMvc.perform(get("/mypage/scrapped-boards")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3));
    }

    @Test
    @DisplayName("닉네임을 업데이트 한다.")
    @Transactional
    void updateNickname() throws Exception {

        //given
        String updateNickname = "업데이트될닉네임";
        Member member = TestDataUtils.createTestMember();

        when(memberService.findById(any())).thenReturn(member);

        //when
        ResultActions resultActions = mockMvc.perform(put("/mypage/account/nickname")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON)
                .content("{\"nickname\": \"" + updateNickname + "\"}")
        );

        //then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("비밀번호를 업데이트 한다.")
    @Transactional
    void updatePassword() throws Exception {

        //given
        String updatePassword = "newPassword12!";
        Member member = TestDataUtils.createTestMember();

        when(memberService.findById(any())).thenReturn(member);

        //when
        ResultActions resultActions = mockMvc.perform(put("/mypage/account/password")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON)
                .content("{\"password\": \"" + updatePassword + "\"}")
        );

        //then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("프로필 사진을 업로드 한다.")
    @Transactional
    void uploadPhoto() throws Exception {

        //given
        MockMultipartFile file = new MockMultipartFile("file", "testFile.jpg", "image/jpg", "Test Contetnt".getBytes());
        Member member =TestDataUtils.createTestMember();

        when(memberService.findById(any())).thenReturn(member);

        //when
        ResultActions resultActions = mockMvc.perform(multipart("/mypage/profile/photo")
                .file(file)
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .contentType(MULTIPART_FORM_DATA)
                .with(request -> { request.setMethod("PUT"); return request; })
        );

        //then
        resultActions.andExpect(status().isNoContent());
        mockMvc.perform(delete("/mypage/profile/photo")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        );
    }

    @Test
    @DisplayName("프로필 사진을 삭제한다.")
    @Transactional
    void deletePhoto() throws Exception {
        //given
        Member member = TestDataUtils.createTestMember();

        when(memberService.findById(any())).thenReturn(member);

        //when
        ResultActions resultActions = mockMvc.perform(delete("/mypage/profile/photo")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        );

        //then
        resultActions.andExpect(status().isNoContent());
    }
}