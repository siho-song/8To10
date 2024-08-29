package show.schedulemanagement.service.board;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.security.dto.MemberDetailsDto;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@Transactional
@DisplayName("댓글 좋아요 서비스")
class ReplyHeartServiceTest {

    @Autowired
    ReplyHeartService replyHeartService;

    @Autowired
    MemberService memberService;

    @Autowired
    ReplyService replyService;

    @BeforeEach
    void setAuthentication(){
        MemberDetailsDto user = new MemberDetailsDto(memberService.loadUserByEmail("normal@example.com"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    @DisplayName("댓글 좋아요 정상 등록")
    void add() {
        Member member = memberService.getAuthenticatedMember();
        Long replyId = 2L;
        Reply reply = replyService.findById(replyId);

        replyHeartService.add(reply, member);

        assertThat(reply.getTotalLike()).isEqualTo(101);
    }

    @Test
    @DisplayName("댓글 좋아요 등록 - 이미 좋아요한 댓글 경우 예외 발생")
    void add_liked() {
        Member member = memberService.getAuthenticatedMember();
        Long replyId = 1L;
        Reply reply = replyService.findById(replyId);

        assertThatThrownBy(() -> replyHeartService.add(reply, member)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("댓글 좋아요 정상 삭제")
    void delete(){
        Long replyId = 1L;
        Member member = memberService.getAuthenticatedMember();
        Reply reply = replyService.findById(replyId);

        replyHeartService.delete(reply, member);

        assertThat(reply.getTotalLike()).isEqualTo(99);
    }

    @Test
    @DisplayName("댓글 좋아요 삭제 - 삭제할 좋아요가 없는 경우 예외 발생")
    void delete_not_exist(){
        Long replyId = 2L;
        Member member = memberService.getAuthenticatedMember();
        Reply reply = replyService.findById(replyId);

        assertThatThrownBy(() -> replyHeartService.delete(reply, member)).isInstanceOf(EntityNotFoundException.class);
    }
}