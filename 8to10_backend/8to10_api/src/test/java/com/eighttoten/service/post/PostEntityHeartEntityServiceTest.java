package com.eighttoten.service.post;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.service.PostHeartService;
import com.eighttoten.exception.DuplicatedException;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.service.MemberDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("게시글 좋아요 서비스 테스트")
@Slf4j
class PostEntityHeartEntityServiceTest {

    @Autowired
    PostHeartService postHeartService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Test
    @DisplayName("게시글 좋아요 정상 등록")
    @WithUserDetails(value = "faithful@example.com")
    void add(){
        Member member = memberDetailsService.getAuthenticatedMember();
        Long postId = 1L;
        postHeartService.save(member, postId); //정상 작동

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_POST));

        assertThat(post.getTotalLike()).isEqualTo(6);
        postHeartService.deleteByMemberIdAndPostId(member.getId(),postId);
    }

    @Test
    @DisplayName("게시글 좋아요 등록 - 이미 좋아요한 게시글인 경우 예외 발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void add_liked(){
        Member member = memberDetailsService.getAuthenticatedMember();
        Long postId = 1L;

        assertThatThrownBy(() -> postHeartService.save(member, postId)).isInstanceOf(DuplicatedException.class);
    }

    @Test
    @DisplayName("게시글 좋아요 삭제")
    @WithUserDetails(value = "normal@example.com")
    void delete(){
        Long postId = 1L;
        Member member = memberDetailsService.getAuthenticatedMember();

        postHeartService.deleteByMemberIdAndPostId(member.getId(), postId);
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_POST));

        assertThat(post.getTotalLike()).isEqualTo(4);

        postHeartService.save(member,postId); //Rollback
    }

    @Test
    @DisplayName("게시글 좋아요 삭제 - 삭제할 좋아요가 없는 경우 예외발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void delete_not_exist(){
        Long postId = 3L;
        Member member = memberDetailsService.getAuthenticatedMember();

        assertThatThrownBy(() -> postHeartService.deleteByMemberIdAndPostId(member.getId(), postId)).isInstanceOf(
                NotFoundEntityException.class);
    }
}