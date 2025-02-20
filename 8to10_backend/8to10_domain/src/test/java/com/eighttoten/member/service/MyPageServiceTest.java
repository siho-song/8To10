package com.eighttoten.member.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostScrap;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.domain.post.repository.PostScrapRepository;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import com.eighttoten.support.PasswordEncoder;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
@DisplayName("마이페이지 서비스 테스트")
class MyPageServiceTest {
    @MockBean
    MemberRepository memberRepository;

    @MockBean
    PostRepository postRepository;

    @MockBean
    ReplyRepository replyRepository;

    @MockBean
    PostScrapRepository postScrapRepository;

    @MockBean
    MultipartFileStorageService multipartFileStorageService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    MyPageService myPageService;

    @Test
    @DisplayName("유저가 스크랩한 게시글을 불러온다.")
    void getScrappedPost() {
        //given
        Member member = TestDataUtils.createTestMember(1L,"test@test.com");
        Post post1 = TestDataUtils.createTestPost(1L,member);
        Post post2 = TestDataUtils.createTestPost(2L,member);
        PostScrap postScrap1 = new PostScrap(null, post1, member, null);
        PostScrap postScrap2 = new PostScrap(null, post2, member, null);
        List<PostScrap> postScraps = List.of(postScrap1, postScrap2);

        when(postScrapRepository.findAllByMemberIdWithPost(member.getId())).thenReturn(postScraps);

        //when
        List<Post> scrappedPost = myPageService.getScrappedPost(member.getId());

        //then
        assertThat(scrappedPost).hasSize(postScraps.size());
    }

    @Test
    @DisplayName("유저의 닉네임을 업데이트 한다.")
    void updateNickname() {
        //given
        Member member = TestDataUtils.createTestMember(1L,"test@test.com");
        String updateNickname = "업데이트 된 닉네임";

        //when
        doNothing().when(memberRepository).update(any());
        myPageService.updateNickname(member, updateNickname);

        //then
        assertThat(member.getNickname()).isEqualTo(updateNickname);
    }

    @Test
    @DisplayName("유저의 비밀번호를 업데이트 한다.")
    void updatePassword() {
        //given
        Member member = TestDataUtils.createTestMember(1L,"test@test.com");
        String newPassword = "newPassword12!";
        doNothing().when(memberRepository).update(member);

        //when
        myPageService.updatePassword(member, newPassword);

        //then
        assertThat(passwordEncoder.matches(newPassword, member.getPassword())).isTrue();
    }

    @Test
    @DisplayName("유저의 프로필 사진을 등록,업데이트 한다.")
    void uploadProfilePhoto() throws IOException {
        //given
        Member member = TestDataUtils.createTestMember(1L,"test@test.com");
        String newImagePath = "newImagePath";
        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "testFile.jpg",
                "image/jpg",
                "Test image content".getBytes());

        //when
        doNothing().when(memberRepository).update(member);
        when(multipartFileStorageService.saveImageFile(any())).thenReturn("newImagePath");
        myPageService.uploadProfilePhoto(member, imageFile);

        //then
        assertThat(member.getProfileImagePath()).isEqualTo(newImagePath);
    }

    @Test
    @DisplayName("유저의 프로필 사진을 삭제 한다.")
    void deleteProfilePhoto(){
        //given
        Member member = TestDataUtils.createTestMember(1L,"test@test.com");

        //when
        doNothing().when(memberRepository).update(member);
        doNothing().when(multipartFileStorageService).deleteFile(any());
        myPageService.deleteProfilePhoto(member);

        //then
        assertThat(member.getProfileImagePath()).isNull();
    }
}