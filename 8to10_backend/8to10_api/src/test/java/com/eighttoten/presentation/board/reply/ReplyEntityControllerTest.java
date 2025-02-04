package com.eighttoten.presentation.board.reply;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.community.dto.reply.AddReplyRequest;
import com.eighttoten.community.dto.reply.ReplyUpdateRequest;
import com.eighttoten.infrastructure.TokenProvider;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("댓글 CRUD")
@Transactional
class ReplyEntityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void init(){
        token = tokenProvider.generateAccessToken("normal@example.com"); // 토큰 생성
    }

    @Test
    @DisplayName("댓글 정상 등록")
    void save_reply() throws Exception {
        AddReplyRequest request = new AddReplyRequest();
        request.setPostId(1L);
        request.setContents("테스트용 댓글");
        request.setParentId(null);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/add")
                .contentType(APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .content(body)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("대댓글 정상 등록")
    void save_nested_reply() throws Exception {
        AddReplyRequest request = new AddReplyRequest();
        request.setPostId(1L);
        request.setContents("테스트용 댓글");
        request.setParentId(1L);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/add")
                .contentType(APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .content(body)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("대댓글 레벨이 1보다 높을 경우 예외 발생")
    void save_nested_reply_level() throws Exception {
        AddReplyRequest request = new AddReplyRequest();
        request.setPostId(1L);
        request.setContents("테스트용 댓글");
        request.setParentId(13L);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/add")
                .contentType(APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .content(body)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("대댓글 게시판과, 부모댓글의 게시판이 다를 경우 예외 발생")
    void save_nested_reply_board() throws Exception {
        AddReplyRequest request = new AddReplyRequest();
        request.setPostId(2L);
        request.setContents("테스트용 댓글");
        request.setParentId(1L);

        String body = objectMapper.writeValueAsString(request);

        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/add")
                .contentType(APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .content(body)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("댓글 삭제")
    void delete() throws Exception {
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/reply/{id}", id)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 수정")
    void update() throws Exception {
        ReplyUpdateRequest request = new ReplyUpdateRequest();
        request.setId(1L);
        request.setContents("수정된 댓글입니다.");

        String body = objectMapper.writeValueAsString(request);
        mockMvc.perform(MockMvcRequestBuilders.put("/community/reply")
                .header("Authorization","Bearer " + token)
                .content(body)
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    @DisplayName("댓글 좋아요 추가")
    void addHeart() throws Exception {
        Long id = 2L;
        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/{id}/heart", id)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("댓글 좋아요 삭제")
    void deleteHeart() throws Exception {
        Long id = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/reply/{id}/heart", id)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isOk());
    }
}