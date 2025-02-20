package com.eighttoten.post.service;

import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.service.PostService;
import com.eighttoten.member.domain.Member;
import com.eighttoten.support.AuthAccessor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("게시글 서비스 통합테스트")
public class PostIntegrationTest {
    @Autowired
    AuthAccessor authAccessor;

    @Autowired
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Test
    @Transactional
    @WithUserDetails("normal@example.com")
    @DisplayName("댓글 등록자와 삭제요청한 클라이언트가 같으면 정상삭제 된다.")
    void deletePost() {
        //given
        Member member = authAccessor.getAuthenticatedMember();
        System.out.println("member = " + member);
        Long postId = 1L;

        //when
        postService.deletePost(member, postId);

        //then
        Assertions.assertThat(postRepository.findById(postId)).isEmpty();
    }
}