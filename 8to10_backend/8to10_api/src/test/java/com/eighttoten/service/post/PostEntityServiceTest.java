package com.eighttoten.service.post;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.community.domain.post.PostDetailInfo;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.service.PostService;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.service.MemberDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@DisplayName("게시글 서비스 테스트")
class PostEntityServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Test
    @DisplayName("댓글 등록자와 삭제요청한 클라이언트가 같으면 정상삭제 된다.")
    @WithUserDetails("normal@example.com")
    void deleteById() {
        Member member = memberDetailsService.getAuthenticatedMember();
        Long postId = 1L;

        postService.deleteById(member, postId);

        assertThatThrownBy(() -> postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_POST)))
                .isInstanceOf(NotFoundEntityException.class);
    }

    @Test
    @DisplayName("게시글 단건 조회")
    @WithUserDetails("normal@example.com")
    void searchBoard(){
        //given
        Member member = memberDetailsService.getAuthenticatedMember();
        Long postId = 1L;

        //when
        PostDetailInfo response = postService.searchPostDetailInfo(member.getId(), postId);

        //then
        assertThat(response).isNotNull();
    }
}