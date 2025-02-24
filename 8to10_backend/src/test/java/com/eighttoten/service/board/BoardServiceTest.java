package com.eighttoten.service.board;

import static org.assertj.core.api.Assertions.*;

import com.eighttoten.community.service.BoardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import com.eighttoten.member.domain.Member;
import com.eighttoten.community.dto.BoardSearchResponse;
import com.eighttoten.infrastructure.security.domain.MemberDetails;
import com.eighttoten.global.exception.NotFoundEntityException;
import com.eighttoten.member.service.MemberService;

@SpringBootTest
@Transactional
@DisplayName("게시글 서비스 테스트")
class BoardServiceTest {

    @Autowired
    BoardService boardService;

    @Autowired
    MemberService memberService;

    @BeforeEach
    public void setAuthentication(){
        MemberDetails user = new MemberDetails(memberService.findByEmail("normal@example.com"));
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    @DisplayName("댓글 등록자와 삭제요청한 클라이언트가 같으면 정상삭제 된다.")
    void deleteById() {
        Member member = memberService.getAuthenticatedMember();
        Long boardId = 1L;

        boardService.deleteById(member, boardId);

        assertThatThrownBy(()->boardService.findById(1L)).isInstanceOf(NotFoundEntityException.class);
    }

    @Test
    @DisplayName("게시글 단건 조회")
    void searchBoard(){
        Member member = memberService.getAuthenticatedMember();
        Long boardId = 1L;

        BoardSearchResponse boardSearchResponse = boardService.searchBoard(1L, member);
        assertThat(boardSearchResponse).isNotNull();
    }
}