package com.eighttoten.post.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.community.domain.post.repository.PostHeartRepository;
import com.eighttoten.community.service.PostHeartService;
import com.eighttoten.exception.DuplicatedException;
import com.eighttoten.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("게시글 좋아요 서비스 테스트")
class PostHeartServiceTest {
    @MockBean
    PostHeartRepository postHeartRepository;

    @Autowired
    PostHeartService postHeartService;

    @Test
    @DisplayName("게시글 좋아요 등록 - 이미 좋아요한 게시글인 경우 예외 발생")
    void add_liked(){
        //given
        Member member = TestDataUtils.createTestMember(null,"test");
        Long postId = 1L;
        when(postHeartRepository.existsByMemberIdAndPostId(any(), any())).thenReturn(true);

        //when,then
        assertThatThrownBy(() -> postHeartService.save(member, postId)).isInstanceOf(DuplicatedException.class);
    }
}