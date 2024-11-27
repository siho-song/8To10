package com.eighttoten.repository.board;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.board.BoardPageRequest;
import com.eighttoten.dto.board.BoardPageResponse;
import com.eighttoten.dto.board.BoardSearchResponse;

public interface BoardRepositoryCustom {
    Page<BoardPageResponse> searchPage(BoardPageRequest cond, Pageable pageable);
    Optional<BoardSearchResponse> searchBoard(Long id, Member member);
}
