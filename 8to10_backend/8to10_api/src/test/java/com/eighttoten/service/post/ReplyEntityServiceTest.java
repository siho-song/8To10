package com.eighttoten.service.post;

import static org.assertj.core.api.Assertions.*;

import com.eighttoten.entity.member.MemberEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.entity.community.Reply;
import com.eighttoten.community.dto.reply.ReplyUpdateRequest;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.member.service.MemberService;
import com.eighttoten.community.service.ReplyService;

@SpringBootTest
@DisplayName("댓글 CRUD")
@Transactional
class ReplyEntityServiceTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("댓글 삭제 - 댓글")
    void deleteReply(){
        MemberEntity memberEntity = memberService.findByEmail("normal@example.com");

        replyService.deleteById(memberEntity,1L);

        assertThatThrownBy(() -> replyService.findById(1L)).isInstanceOf(NotFoundEntityException.class);
    }

    @Test
    @DisplayName("댓글 삭제 - 클라이언트와 작성자 이메일 불일치")
    void deleteNotEqualEmail(){
        MemberEntity memberEntity = memberService.findByEmail("normal@example.com");

        assertThatThrownBy(() -> replyService.deleteById(memberEntity, 4L)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("댓글 삭제 - 대댓글")
    void deleteNestedReply(){
        MemberEntity memberEntity = memberService.findByEmail("normal@example.com");

        replyService.deleteById(memberEntity,14L);

        assertThatThrownBy(() -> replyService.findById(14L)).isInstanceOf(NotFoundEntityException.class);
    }

    @Test
    @DisplayName("댓글 수정")
    void updateReply(){
        MemberEntity memberEntity = memberService.findByEmail("normal@example.com");
        ReplyUpdateRequest updateRequest = new ReplyUpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setContents("수정된 댓글");

        replyService.update(memberEntity,updateRequest);
        Reply updatedReply = replyService.findById(updateRequest.getId());

        assertThat(updatedReply.getContents()).isEqualTo(updateRequest.getContents());
    }
}