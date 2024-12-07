package com.eighttoten.repository.board;

import com.eighttoten.domain.member.Member;
import com.eighttoten.dto.board.BoardPageRequest;
import com.eighttoten.dto.board.BoardPageResponse;
import com.eighttoten.dto.board.BoardSearchResponse;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Optional<BoardSearchResponse> searchBoard(Long id, Member member);
    Page<BoardPageResponse> searchPage(BoardPageRequest cond, Pageable pageable);
}