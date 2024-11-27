package com.eighttoten.service.board;

import java.util.List;
import com.eighttoten.domain.board.BoardScrap;
import com.eighttoten.domain.member.Member;

public interface BoardScrapService {
    BoardScrap findByMemberAndBoardId(Member member, Long boardId);
    boolean existsByMemberAndBoardId(Member member, Long boardId);
    void add(Member member, Long boardId);
    void delete(Member member, Long boardId);
    List<BoardScrap> findAllByMemberWithBoard(Member member);
}
