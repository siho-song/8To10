package com.eighttoten.post.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.eighttoten.community.domain.reply.NewReplyHeart;
import com.eighttoten.community.domain.reply.repository.ReplyHeartRepository;
import com.eighttoten.community.service.ReplyHeartService;
import com.eighttoten.exception.DuplicatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@DisplayName("댓글 좋아요 서비스")
class ReplyHeartServiceTest {
    @MockBean
    ReplyHeartRepository replyHeartRepository;

    @Autowired
    ReplyHeartService replyHeartService;

    @Test
    @DisplayName("댓글 좋아요 등록 - 이미 좋아요한 댓글 경우 예외 발생")
    void add_ReplyHeart_liked() {
        //given
        NewReplyHeart newReplyHeart = NewReplyHeart.from(null, null);
        when(replyHeartRepository.existsByMemberIdAndReplyId(any(), any())).thenReturn(true);

        //when,then
        assertThatThrownBy(() -> replyHeartService.addReplyHeart(newReplyHeart)).isInstanceOf(DuplicatedException.class);
    }

    //TODO 댓글 좋아요 등록 성공시 eventPublisher 호출
}