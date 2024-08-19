package show.schedulemanagement.service.board;

import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import show.schedulemanagement.domain.board.reply.Reply;
import show.schedulemanagement.domain.member.Member;
import show.schedulemanagement.service.MemberService;

@SpringBootTest
@DisplayName("댓글 CRUD")
class ReplyServiceImplTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("이메일로 댓글과 댓글의 게시판,멤버 함께 조회")
    void findAllWithBoardAndMemberByEmail() {
        Member member = memberService.findByEmail("normal@example.com");
        List<Reply> allByMemberEmail = replyService.findAllWithBoardAndMemberByEmail(member);
        assertThat(allByMemberEmail.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("댓글 삭제 - 댓글")
    void deleteReply(){
        Member member = memberService.findByEmail("normal@example.com");
        replyService.delete(member,1L);
        assertThatThrownBy(() -> replyService.findById(1L)).isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    @DisplayName("댓글 삭제 - 클라이언트와 작성자 이메일 불일치")
    void deleteNotEqualEmail(){
        Member member = memberService.findByEmail("normal@example.com");
        assertThatThrownBy(() -> replyService.delete(member, 1L)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("댓글 삭제 - 대댓글")
    void deleteNestedReply(){
        Member member = memberService.findByEmail("normal@example.com");
        replyService.delete(member,14L);
    }
}