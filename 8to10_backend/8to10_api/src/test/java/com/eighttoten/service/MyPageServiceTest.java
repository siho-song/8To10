package com.eighttoten.service;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.PostScrap;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.domain.post.repository.PostScrapRepository;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.member.domain.Member;
import com.eighttoten.member.domain.MemberRepository;
import com.eighttoten.member.service.MultipartFileStorageService;
import com.eighttoten.member.service.MyPageService;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;

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

    @Autowired
    MyPageService myPageService;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Autowired
    MultipartFileStorageService multipartFileStorageService;

    @Test
    @DisplayName("유저가 작성한 게시글을 불러온다.")
    @WithUserDetails("normal@example.com")
    void getWrittenPosts() {
        //given
        Member member = memberDetailsService.getAuthenticatedMember();
        Post post1 = TestDataUtils.createTestPost(member);
        Post post2 = TestDataUtils.createTestPost(member);
        List<Post> posts = List.of(post1, post2);

        when(postRepository.findAllByMemberId(member.getId())).thenReturn(posts);

        //when
        List<Post> writtenPosts = myPageService.getWrittenPosts(member.getId());

        //then
        assertThat(writtenPosts).hasSize(posts.size());
    }

    @Test
    @DisplayName("유저가 작성한 댓글을 불러온다.")
    @WithUserDetails("normal@example.com")
    void getWrittenReplies() {
        //given
        Member member = memberDetailsService.getAuthenticatedMember();
        Reply reply1 = TestDataUtils.createTestReply(null, member.getEmail());
        Reply reply2 = TestDataUtils.createTestReply(null, member.getEmail());
        List<Reply> replies = List.of(reply1, reply2);

        when(replyRepository.findAllByMemberId(member.getId())).thenReturn(replies);

        //when
        List<Reply> memberReplies = myPageService.getWrittenReplies(member.getId());

        //then
        assertThat(memberReplies).hasSize(replies.size());
    }

    @Test
    @DisplayName("유저가 스크랩한 게시글을 불러온다.")
    @WithUserDetails("normal@example.com")
    void getScrappedPost() {
        //given
        Member member = memberDetailsService.getAuthenticatedMember();
        PostScrap postScrap1 = new PostScrap(null, null, member, null);
        PostScrap postScrap2 = new PostScrap(null, null, member, null);
        List<PostScrap> postScraps = List.of(postScrap1, postScrap2);

        when(postScrapRepository.findAllByMemberIdWithPost(member.getId())).thenReturn(postScraps);

        //when
        List<Post> scrappedPost = myPageService.getScrappedPost(member.getId());

        //then
        assertThat(scrappedPost).hasSize(postScraps.size());
    }

    @Test
    @DisplayName("유저의 닉네임을 업데이트 한다.")
    @WithUserDetails("normal@example.com")
    void updateNickname() {
        //given
        Member member = memberDetailsService.getAuthenticatedMember();
        String beforeNickname = member.getNickname();
        String updateNickname = "업데이트 된 닉네임";

        //when
        doNothing().when(memberRepository).update(any());
        myPageService.updateNickname(member, updateNickname);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        //then
        Member updatedMember = memberRepository.findById(member.getId()).get();
        assertThat(updatedMember.getNickname()).isEqualTo(updateNickname);
    }

    @Test
    @DisplayName("유저의 비밀번호를 업데이트 한다.")
    @WithUserDetails("normal@example.com")
    void updatePassword() {
        //given
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Member member = memberDetailsService.getAuthenticatedMember();
        String updatePassword = "newPassword12!";

        //when
        doNothing().when(memberRepository).update(member);
        myPageService.updatePassword(member, updatePassword);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        //then
        Member updatedMember = memberRepository.findById(member.getId()).get();
        assertThat(encoder.matches(updatePassword, updatedMember.getPassword())).isTrue();
    }

    @Test
    @DisplayName("유저의 프로필 사진을 등록,업데이트 한다.")
    @WithUserDetails("normal@example.com")
    void uploadProfilePhoto() throws IOException {
        //given
        Member member = memberDetailsService.getAuthenticatedMember();
        MockMultipartFile imageFile = new MockMultipartFile(
                "file",
                "testFile.jpg",
                "image/jpg",
                "Test image content".getBytes());

        //when
        doNothing().when(memberRepository).update(member);
        myPageService.uploadProfilePhoto(member, imageFile);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        //then
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember.getProfileImagePath()).isNotNull();
    }

    @Test
    @DisplayName("유저의 프로필 사진을 삭제 한다.")
    @WithUserDetails("normal@example.com")
    void deleteProfilePhoto(){
        //given
        Member member = memberDetailsService.getAuthenticatedMember();

        //when
        doNothing().when(memberRepository).update(member);
        myPageService.deleteProfilePhoto(member);
        when(memberRepository.findById(any())).thenReturn(Optional.of(member));

        //then
        Member findMember = memberRepository.findById(member.getId()).get();
        assertThat(findMember.getProfileImagePath()).isNull();
    }
}