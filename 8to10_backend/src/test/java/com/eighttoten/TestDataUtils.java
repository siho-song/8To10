package com.eighttoten;

import com.eighttoten.domain.board.Board;
import com.eighttoten.domain.board.BoardScrap;
import com.eighttoten.domain.board.reply.Reply;
import com.eighttoten.domain.member.Gender;
import com.eighttoten.domain.member.Member;
import com.eighttoten.domain.member.Mode;
import com.eighttoten.dto.board.BoardSaveRequest;
import com.eighttoten.dto.signup.SignUpRequest;

public class TestDataUtils {
    public static Member createTestMember() {
        SignUpRequest signUpRequest = SignUpRequest.builder()
                .username("testUsername")
                .nickname("testNickname")
                .email("normal@example.com")
                .password("password1")
                .phoneNumber("000-0000-0000")
                .mode(Mode.MILD)
                .gender(Gender.MALE)
                .isAuthEmail(false)
                .isAuthPhone(false)
                .build();
        return Member.from(signUpRequest);
    }

    public static Reply createTestReply(Reply parent,Member member,Board board){
        return Reply.of(parent, "테스트 댓글 내용", member, board);
    }
    public static Board createTestBoard(Member member) {
        BoardSaveRequest dto = new BoardSaveRequest("테스트 데이터", "테스트 데이터 내용");
        return Board.from(member, dto);
    }

    public static BoardScrap createTestBoardScrap(Board board, Member member) {
        return BoardScrap.of(board, member);
    }
}