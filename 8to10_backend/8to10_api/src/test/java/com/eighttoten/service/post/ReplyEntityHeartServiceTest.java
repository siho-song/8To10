package com.eighttoten.service.post;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.eighttoten.community.domain.reply.repository.ReplyRepository;
import com.eighttoten.member.domain.Member;
import com.eighttoten.service.MemberDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.exception.NotFoundEntityException;
import com.eighttoten.community.service.ReplyHeartService;

@SpringBootTest
@DisplayName("댓글 좋아요 서비스")
class ReplyEntityHeartServiceTest {

    @Autowired
    ReplyHeartService replyHeartService;

    @Autowired
    MemberDetailsService memberDetailsService;

    @Autowired
    ReplyRepository replyRepository;

    @Test
    @DisplayName("댓글 좋아요 정상 등록")
    @WithUserDetails(value = "normal@example.com")
    void add() {
        Member member = memberDetailsService.getAuthenticatedMember();
        Long replyId = 2L;

        replyHeartService.add(member, replyId);
        Reply reply = replyRepository.findById(replyId);

        assertThat(reply.getTotalLike()).isEqualTo(101);
        replyHeartService.deleteByMemberIdAndReplyId(replyId, member);
    }

    @Test
    @DisplayName("댓글 좋아요 등록 - 이미 좋아요한 댓글 경우 예외 발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void add_liked() {
        MemberEntity memberEntity = memberDetailsService.getAuthenticatedMember();
        Long replyId = 1L;

        assertThatThrownBy(() -> replyHeartService.add(replyId, memberEntity)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("댓글 좋아요 정상 삭제")
    @WithUserDetails(value = "normal@example.com")
    void delete(){
        Long replyId = 1L;
        MemberEntity memberEntity = memberDetailsService.getAuthenticatedMember();

        replyHeartService.deleteByMemberIdAndReplyId(replyId, memberEntity);
        Reply reply = replyRepository.findById(replyId);

        assertThat(reply.getTotalLike()).isEqualTo(99);
        replyHeartService.add(replyId, memberEntity);
    }

    @Test
    @DisplayName("댓글 좋아요 삭제 - 삭제할 좋아요가 없는 경우 예외 발생")
    @Transactional
    @WithUserDetails(value = "normal@example.com")
    void delete_not_exist(){
        Long replyId = 2L;
        MemberEntity memberEntity = memberDetailsService.getAuthenticatedMember();

        assertThatThrownBy(() -> replyHeartService.deleteByMemberIdAndReplyId(replyId, memberEntity)).isInstanceOf(NotFoundEntityException.class);
    }
}