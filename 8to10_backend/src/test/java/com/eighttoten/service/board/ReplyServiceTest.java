package com.eighttoten.service.board;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.community.domain.Reply;
import com.eighttoten.member.domain.Member;
import com.eighttoten.community.dto.ReplyUpdateRequest;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.service.MemberService;
import com.eighttoten.community.service.ReplyService;

@SpringBootTest
@DisplayName("댓글 CRUD")
@Transactional
class ReplyServiceTest {

    @Autowired
    ReplyService replyService;
    @Autowired
    MemberService memberService;

    @Test
    @DisplayName("이메일로 댓글과 댓글의 게시판,멤버 함께 조회")
    void findAllWithBoardAndMemberByEmail() { //TODO 테스트 수정
        Member member = memberService.findByEmail("normal@example.com");

        List<Reply> allByMemberEmail = replyService.findAllWithBoardAndMemberByEmail(member);

        assertThat(allByMemberEmail.size()).isEqualTo(10);
    }

    @Test
    @DisplayName("댓글 삭제 - 댓글")
    void deleteReply(){
        Member member = memberService.findByEmail("normal@example.com");

        replyService.delete(member,1L);

        assertThatThrownBy(() -> replyService.findById(1L)).isInstanceOf(NotFoundEntityException.class);
    }

    @Test
    @DisplayName("댓글 삭제 - 클라이언트와 작성자 이메일 불일치")
    void deleteNotEqualEmail(){
        Member member = memberService.findByEmail("normal@example.com");

        assertThatThrownBy(() -> replyService.delete(member, 4L)).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("댓글 삭제 - 대댓글")
    void deleteNestedReply(){
        Member member = memberService.findByEmail("normal@example.com");

        replyService.delete(member,14L);

        assertThatThrownBy(() -> replyService.findById(14L)).isInstanceOf(NotFoundEntityException.class);
    }

    @Test
    @DisplayName("댓글 수정")
    void updateReply(){
        Member member = memberService.findByEmail("normal@example.com");
        ReplyUpdateRequest updateRequest = new ReplyUpdateRequest();
        updateRequest.setId(1L);
        updateRequest.setContents("수정된 댓글");

        replyService.update(member,updateRequest);
        Reply updatedReply = replyService.findById(updateRequest.getId());

        assertThat(updatedReply.getContents()).isEqualTo(updateRequest.getContents());
    }
}