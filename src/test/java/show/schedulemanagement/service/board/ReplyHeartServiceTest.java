package show.schedulemanagement.service.board;

import static org.assertj.core.api.Assertions.assertThat;

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
    void addHeart() {
        Member member = memberService.getAuthenticatedMember();
        Long replyId = 2L;
        Reply reply = replyService.findById(replyId);

        replyHeartService.addHeart(reply, member);

        assertThat(reply.getTotalHearts()).isEqualTo(101);
    }
}