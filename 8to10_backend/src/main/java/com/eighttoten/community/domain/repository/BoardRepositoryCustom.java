package com.eighttoten.community.domain.repository;

import com.eighttoten.community.dto.BoardPageRequest;
import com.eighttoten.community.dto.BoardPageResponse;
import com.eighttoten.community.dto.BoardSearchResponse;
import com.eighttoten.member.domain.Member;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    Optional<BoardSearchResponse> searchBoard(Long id, Member member);
    Page<BoardPageResponse> searchPage(BoardPageRequest cond, Pageable pageable);
}