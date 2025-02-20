package com.eighttoten.post.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.UpdatePost;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.service.PostService;
import com.eighttoten.member.domain.Member;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("게시글 서비스 테스트")
class PostServiceTest {
    @MockBean
    PostRepository postRepository;

    @Autowired
    PostService postService;

    @Test
    @DisplayName("게시글 업데이트 - 클라이언트의 이메일과, 게시글 등록자의 이메일이 같으면 성공한다.")
    void searchPostDetail(){
        //given
        Long postId = 1L;
        Member member = TestDataUtils.createTestMember(null, "test");
        Post post = TestDataUtils.createTestPost(postId, member);
        String newTitle = "새로운 제목";
        String newContents = "새로운 내용";
        when(postRepository.findById(any())).thenReturn(Optional.of(post));

        //when
        postService.updatePost(member, new UpdatePost(postId, newTitle, newContents));

        //then
        assertThat(post.getTitle()).isEqualTo(newTitle);
        assertThat(post.getContents()).isEqualTo(newContents);
    }
}