package com.eighttoten.post.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.eighttoten.TestDataUtils;
import com.eighttoten.community.domain.reply.Reply;
import com.eighttoten.community.domain.reply.UpdateReply;
import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.community.service.ReplyService;
import com.eighttoten.member.domain.Member;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@DisplayName("댓글 서비스 테스트")
@Transactional
class ReplyServiceTest {
    @MockBean
    ReplyRepository replyRepository;

    @Autowired
    ReplyService replyService;

    @Test
    @DisplayName("댓글 내용을 수정한다")
    void updateReply(){
        //given
        Member member = TestDataUtils.createTestMember(null, "test");
        Reply reply = TestDataUtils.createTestReply(null, null, member.getEmail());
        UpdateReply updateReply = new UpdateReply(null, "댓글 업데이트 테스트");
        when(replyRepository.findById(any())).thenReturn(Optional.of(reply));
        doNothing().when(replyRepository).update(any());

        replyService.update(member,updateReply);
        Assertions.assertThat(reply.getContents()).isEqualTo(updateReply.getContents());
    }
}