package show.schedulemanagement.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.Board;
import show.schedulemanagement.domain.board.BoardScrap;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.provider.TokenProvider;
import show.schedulemanagement.service.board.BoardScrapService;
import show.schedulemanagement.service.board.BoardService;
import show.schedulemanagement.service.board.reply.ReplyService;

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

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    String token;

    @BeforeEach
    void init() {
        token = tokenProvider.generateAccessToken("normal@example.com");
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
        Board board1 = Board.builder().id(1L).build();
        Board board2 = Board.builder().id(2L).build();
        Board board3 = Board.builder().id(3L).build();

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
        Board board1 = Board.builder().id(1L).build();
        Board board2 = Board.builder().id(2L).build();
        Board board3 = Board.builder().id(3L).build();

        Reply reply1 = Reply.builder().id(1L).board(board1).build();
        Reply reply2 = Reply.builder().id(2L).board(board2).build();
        Reply reply3 = Reply.builder().id(3L).board(board3).build();

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
        Board board1 = Board.builder().id(1L).build();
        Board board2 = Board.builder().id(2L).build();
        Board board3 = Board.builder().id(3L).build();

        BoardScrap boardScrap1 = BoardScrap.builder().id(1L).board(board1).build();
        BoardScrap boardScrap2 = BoardScrap.builder().id(2L).board(board2).build();
        BoardScrap boardScrap3 = BoardScrap.builder().id(3L).board(board3).build();

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
}