package com.eighttoten.service.board;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.eighttoten.domain.board.Board;
import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.board.BoardPageRequest;
import com.eighttoten.dto.board.BoardPageResponse;
import com.eighttoten.dto.board.BoardSearchResponse;
import com.eighttoten.dto.board.BoardUpdateRequest;

public interface BoardService {
    void save(Board board);
    Board findById(Long id);
    Board findByIdWithMember(Long id);
    Board findByIdWithRepliesAndMember(Long id);
    List<Board> findAllByMember(Member member);
    Page<BoardPageResponse> searchBoardPage(BoardPageRequest searchCond, Pageable pageable);
    BoardSearchResponse searchBoard(Long id, Member member);
    void deleteById(Member member, Long id);
    void update(Member member, BoardUpdateRequest updateRequest);
}
