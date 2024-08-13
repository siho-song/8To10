package show.schedulemanagement.service.board;

import static org.assertj.core.api.Assertions.*;

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
}