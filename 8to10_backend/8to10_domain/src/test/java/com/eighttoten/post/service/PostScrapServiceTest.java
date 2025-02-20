package com.eighttoten.post.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.community.domain.post.repository.PostRepository;
import com.eighttoten.community.domain.post.repository.PostScrapRepository;
import com.eighttoten.community.service.PostScrapService;
import com.eighttoten.exception.DuplicatedException;
import com.eighttoten.member.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("게시글 스크랩 서비스")
class PostScrapServiceTest {

    @MockBean
    PostScrapRepository postScrapRepository;

    @Autowired
    PostScrapService postScrapService;

    @Autowired
    PostRepository postRepository;

    @Test
    @DisplayName("게시글 스크랩 등록 실패 - 이미 스크랩한 게시글 예외 발생")
    void add_scraped_exist() {
        //given
        Member member = TestDataUtils.createTestMember(null, "test");
        Long postId = 1L;
        when(postScrapRepository.existsByMemberIdAndPostId(any(), any())).thenReturn(true);

        assertThatThrownBy(() -> postScrapService.save(member, postId)).isInstanceOf(DuplicatedException.class);
    }
}