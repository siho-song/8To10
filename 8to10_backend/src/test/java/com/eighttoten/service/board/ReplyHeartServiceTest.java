package com.eighttoten.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.community.domain.Reply;
import com.eighttoten.member.domain.Member;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.service.MemberService;
import com.eighttoten.community.service.ReplyHeartService;
import com.eighttoten.community.service.ReplyService;

@SpringBootTest
@DisplayName("댓글 좋아요 서비스")
class ReplyHeartServiceTest {

    @Autowired
    ReplyHeartService replyHeartService;

    @Autowired
    MemberService memberService;

    @Autowired
    ReplyService replyService;

    @Test
    @DisplayName("댓글 좋아요 정상 등록")
    @WithUserDetails(value = "normal@example.com")
    void add() {
        Member member = memberService.getAuthenticatedMember();
        Long replyId = 2L;

        replyHeartService.add(replyId, member);
        Reply reply = replyService.findById(replyId);

        assertThat(reply.getTotalLike()).isEqualTo(101);
        replyHeartService.delete(replyId,member);
    }

    @Test
    @DisplayName("댓글 좋아요 등록 - 이미 좋아요한 댓글 경우 예외 발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void add_liked() {
        Member member = memberService.getAuthenticatedMember();
        Long replyId = 1L;

        assertThatThrownBy(() -> replyHeartService.add(replyId, member)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("댓글 좋아요 정상 삭제")
    @WithUserDetails(value = "normal@example.com")
    void delete(){
        Long replyId = 1L;
        Member member = memberService.getAuthenticatedMember();

        replyHeartService.delete(replyId, member);
        Reply reply = replyService.findById(replyId);

        assertThat(reply.getTotalLike()).isEqualTo(99);
        replyHeartService.add(replyId, member);
    }

    @Test
    @DisplayName("댓글 좋아요 삭제 - 삭제할 좋아요가 없는 경우 예외 발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void delete_not_exist(){
        Long replyId = 2L;
        Member member = memberService.getAuthenticatedMember();

        assertThatThrownBy(() -> replyHeartService.delete(replyId, member)).isInstanceOf(NotFoundEntityException.class);
    }
}