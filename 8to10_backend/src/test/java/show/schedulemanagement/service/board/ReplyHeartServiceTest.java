package show.schedulemanagement.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.service.MemberService;
import show.schedulemanagement.service.board.reply.ReplyHeartService;
import show.schedulemanagement.service.board.reply.ReplyService;

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

        assertThatThrownBy(() -> replyHeartService.delete(replyId, member)).isInstanceOf(EntityNotFoundException.class);
    }
}