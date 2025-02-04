package com.eighttoten.service.post;

import static com.eighttoten.exception.ExceptionCode.NOT_FOUND_POST;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.community.domain.post.Post;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.service.PostScrapService;
import com.eighttoten.exception.DuplicatedException;
import com.eighttoten.member.domain.Member;
import com.eighttoten.service.MemberDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.exception.NotFoundEntityException;

@SpringBootTest
@DisplayName("게시글 스크랩 서비스")
class PostEntityScrapServiceTest {

    @Autowired
    PostScrapService postScrapService;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("게시글 스크랩 정상 등록")
    @WithUserDetails(value = "normal@example.com")
    void add() {
        Member member = memberDetailsService.getAuthenticatedMember();
        Long postId = 2L;
        postScrapService.save(member, postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_POST));

        assertThat(post.getTotalScrap()).isEqualTo(2);

        postScrapService.deleteByMemberIdAndPostId(member.getId(), postId);
    }

    @Test
    @DisplayName("게시글 스크랩 등록 실패 - 이미 스크랩한 게시글 예외 발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void add_scraped(){
        Member member = memberDetailsService.getAuthenticatedMember();
        Long postId = 1L;

        assertThatThrownBy(() -> postScrapService.save(member, postId)).isInstanceOf(DuplicatedException.class);
    }

    @Test
    @DisplayName("게시글 스크랩 정상 삭제")
    @WithUserDetails(value = "normal@example.com")
    void delete(){
        Member member = memberDetailsService.getAuthenticatedMember();

        Long postId = 1L;
        postScrapService.deleteByMemberIdAndPostId(member.getId(), postId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundEntityException(NOT_FOUND_POST));

        assertThat(post.getTotalScrap()).isEqualTo(1L);

        postScrapService.save(member,postId);
    }

    @Test
    @DisplayName("게시글 스크랩 삭제 실패 - 삭제할 스크랩이 없는 경우 예외발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void delete_not_exist(){
        Member member = memberDetailsService.getAuthenticatedMember();

        Long postId = 2L;
        assertThatThrownBy(() -> postScrapService.deleteByMemberIdAndPostId(member.getId(), postId)).isInstanceOf(
                NotFoundEntityException.class);
    }
}