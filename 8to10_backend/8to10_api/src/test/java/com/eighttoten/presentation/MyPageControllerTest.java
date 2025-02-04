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
import com.eighttoten.support.TokenProvider;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostScrap;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.domain.post.repository.PostScrapRepository;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.service.ReplyService;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import java.util.List;
import java.util.Optional;
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

@AutoConfigureMockMvc
@SpringBootTest
@DisplayName("마이페이지 컨트롤러 테스트")
class MyPageControllerTest {

    @MockBean
    PostRepository postRepository;

    @MockBean
    ReplyService replyService;

    @MockBean
    PostScrapRepository postScrapRepository;

    @MockBean
    MemberRepository memberRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    TokenProvider tokenProvider;

    String token;

    @BeforeEach
    void init() {
        token = tokenProvider.generateAccessToken("normal@example.com");
        when(memberRepository.findById(any())).thenReturn(Optional.of(TestDataUtils.createTestMember()));
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
    void getMemberPosts() throws Exception {
        Member member = TestDataUtils.createTestMember();
        //given
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
        Member member = TestDataUtils.createTestMember();
        //given
        String email = member.getEmail();
        Reply reply1 = TestDataUtils.createTestReply(null, email);
        Reply reply2 = TestDataUtils.createTestReply(null, email);
        Reply reply3 = TestDataUtils.createTestReply(null, email);

        when(replyService.findAllByMemberId(any())).thenReturn(List.of(reply1, reply2, reply3));

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
        Member member = TestDataUtils.createTestMember();
        //given
        Post post1 = TestDataUtils.createTestPost(member);
        Post post2 = TestDataUtils.createTestPost(member);
        Post post3 = TestDataUtils.createTestPost(member);

        PostScrap postScrap1 = TestDataUtils.createTestPostScrap(member, post1);
        PostScrap postScrap2 = TestDataUtils.createTestPostScrap(member, post2);
        PostScrap postScrap3 = TestDataUtils.createTestPostScrap(member, post3);

        when(postScrapRepository.findAllByMemberIdWithPost(any())).thenReturn(
                List.of(postScrap1, postScrap2, postScrap3));

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
    @DisplayName("닉네임을 업데이트 한다.")
    @Transactional
    void updateNickname() throws Exception {
        //given
        String updateNickname = "업데이트될닉네임";
        Member member = TestDataUtils.createTestMember();

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

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

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

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

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

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

        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        //when
        ResultActions resultActions = mockMvc.perform(delete("/mypage/profile/photo")
                .accept(APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
        );

        //then
        resultActions.andExpect(status().isNoContent());
    }
}