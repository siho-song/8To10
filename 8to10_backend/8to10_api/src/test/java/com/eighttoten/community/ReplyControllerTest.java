package com.eighttoten.community;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eighttoten.support.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("댓글 컨트롤러 통합 테스트")
class ReplyControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    @Autowired
    ObjectMapper objectMapper;

    private String token;

    @BeforeEach
    void init(){
        token = tokenProvider.generateAccessToken("normal@example.com");
    }

    @Test
    @DisplayName("특정 포스트에 대한 멤버의 댓글 등록이 성공한다.")
    void save_reply() throws Exception {
        //given
        String token1 = tokenProvider.generateAccessToken("normal2@example.com");
        long postId = 1L;
        String contents = "테스트용 댓글";
        Map<String, Object> request = new HashMap<>();
        request.put("postId", postId);
        request.put("contents", contents);
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/save")
                .contentType(APPLICATION_JSON)
                .header("Authorization","Bearer " + token1)
                .content(body)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("특정 포스트에 대한 멤버의 대댓글 등록이 성공한다.")
    void save_nested_reply() throws Exception {
        //given
        long postId = 1L;
        long parentId = 2L;
        String contents = "테스트용 댓글";
        Map<String, Object> request = new HashMap<>();
        request.put("postId", postId);
        request.put("contents", contents);
        request.put("parentId", parentId);
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/save")
                .contentType(APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .content(body)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("특정 포스트에 달려는 대댓글의 레벨이 1보다 높을 경우 4XX 예외가 발생한다.")
    void save_nested_reply_level() throws Exception {
        //given
        long postId = 1L;
        long parentId = 13L;
        String contents = "테스트용 댓글";
        Map<String, Object> request = new HashMap<>();
        request.put("postId", postId);
        request.put("contents", contents);
        request.put("parentId", parentId);
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/add")
                .contentType(APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .content(body)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("대댓글 게시판과, 부모댓글의 게시판이 다를 경우 4XX 예외가 발생한다.")
    void save_nested_reply_board() throws Exception {
        //given
        long postId = 2L;
        long parentId = 1L;
        String contents = "테스트용 댓글";
        Map<String, Object> request = new HashMap<>();
        request.put("postId", postId);
        request.put("contents", contents);
        request.put("parentId", parentId);
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/add")
                .contentType(APPLICATION_JSON)
                .header("Authorization","Bearer " + token)
                .content(body)
        ).andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("특정 포스트에 멤버가 달았던 댓글을 댓글의 id로 삭제한다.")
    void deleteReply() throws Exception {
        //given
        Long replyId = 1L;

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/reply/{id}", replyId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("멤버가 달았던 댓글을 댓글의 id로 수정한다.")
    void updateReply() throws Exception {
        //given
        long replyId = 2L;
        String newContents = "수정된 댓글입니다.";
        Map<String, Object> request = new HashMap<>();
        request.put("id", replyId);
        request.put("contents", newContents);
        String body = objectMapper.writeValueAsString(request);

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.put("/community/reply")
                .header("Authorization","Bearer " + token)
                .content(body)
                .contentType(APPLICATION_JSON)
        ).andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("특정 댓글에 멤버의 좋아요를 추가한다.")
    void addHeart() throws Exception {
        //given
        Long replyId = 2L;

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.post("/community/reply/{id}/heart", replyId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isCreated());
    }

    @Test
    @DisplayName("특정 댓글에 존재하는 멤버의 좋아요를 삭제한다.")
    void deleteHeart() throws Exception {
        //given
        Long replyId = 1L;

        //when,then
        mockMvc.perform(MockMvcRequestBuilders.delete("/community/reply/{id}/heart", replyId)
                .header("Authorization","Bearer " + token)
        ).andExpect(status().isNoContent());
    }
}