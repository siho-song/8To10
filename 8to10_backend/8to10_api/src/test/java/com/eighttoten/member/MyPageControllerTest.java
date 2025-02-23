package com.eighttoten.member;

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
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostScrap;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.domain.post.repository.PostScrapRepository;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.member.domain.Member;
import com.eighttoten.support.TokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("마이페이지 컨트롤러 테스트")
class MyPageControllerTest {
    @MockBean
    PostRepository postRepository;

    @MockBean
    ReplyRepository replyRepository;

    @MockBean
    PostScrapRepository postScrapRepository;

    @Autowired
    ObjectMapper objectMapper;

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
        //when
        ResultActions resultActions = mockMvc.perform(get("/mypage")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickname").isNotEmpty())
                .andExpect(jsonPath("$.email").isNotEmpty())
                .andExpect(jsonPath("$.role").isNotEmpty());
    }

    @Test
    @DisplayName("유저가 작성한 게시글을 불러온다.")
    void getMemberPosts() throws Exception {
        //given
        Member member = TestDataUtils.createTestMember();
        Post post1 = TestDataUtils.createTestPost(member);
        Post post2 = TestDataUtils.createTestPost(member);
        Post post3 = TestDataUtils.createTestPost(member);
        when(postRepository.findAllByMemberId(any())).thenReturn(List.of(post1, post2, post3));

        //when
        ResultActions resultActions = mockMvc.perform(get("/mypage/posts")
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
        String email = "test";
        Reply reply1 = TestDataUtils.createTestReply(null,null,email);
        Reply reply2 = TestDataUtils.createTestReply(null,null, email);
        Reply reply3 = TestDataUtils.createTestReply(null,null, email);

        when(replyRepository.findAllByMemberId(any())).thenReturn(List.of(reply1, reply2, reply3));

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
    void getScrappedPosts() throws Exception {
        //given
        Member member = TestDataUtils.createTestMember();
        Post post1 = TestDataUtils.createTestPost(member);
        Post post2 = TestDataUtils.createTestPost(member);
        Post post3 = TestDataUtils.createTestPost(member);

        PostScrap postScrap1 = TestDataUtils.createTestPostScrap(member, post1);
        PostScrap postScrap2 = TestDataUtils.createTestPostScrap(member, post2);
        PostScrap postScrap3 = TestDataUtils.createTestPostScrap(member, post3);

        when(postScrapRepository.findAllByMemberIdWithPost(any())).thenReturn(List.of(postScrap1, postScrap2, postScrap3));

        //when
        ResultActions resultActions = mockMvc.perform(get("/mypage/scrapped-posts")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        );

        //then
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.items.length()").value(3));
    }

    @Test
    @DisplayName("멤버의 닉네임을 업데이트 한다.")
    void updateNickname() throws Exception {
        //given
        Map<String, Object> request = new HashMap<>();
        String nickName = "업데이트될닉네임";
        request.put("nickname",nickName);
        String body = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(put("/mypage/account/nickname")
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON)
                .content(body)
        );

        //then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("멤버의 비밀번호를 업데이트 한다.")
    void updatePassword() throws Exception {
        //given
        Map<String, Object> request = new HashMap<>();
        request.put("password","newPassword12!");
        String body = objectMapper.writeValueAsString(request);

        //when
        ResultActions resultActions = mockMvc.perform(put("/mypage/account/password")
                .header("Authorization", "Bearer " + token)
                .contentType(APPLICATION_JSON)
                .content(body)
        );

        //then
        resultActions.andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("프로필 사진을 업로드 한 뒤 삭제 한다.")
    void uploadPhoto() throws Exception {
        //given
        MockMultipartFile file = new MockMultipartFile("file", "testFile.jpg", "image/jpg", "Test Contetnt".getBytes());

        //when,then
        mockMvc.perform(multipart("/mypage/profile/photo")
                .file(file)
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .contentType(MULTIPART_FORM_DATA)
                .with(request -> {
                    request.setMethod("PUT");
                    return request;
                })
        ).andExpect(status().isNoContent());

        mockMvc.perform(delete("/mypage/profile/photo")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        ).andExpect(status().isNoContent());
    }
}