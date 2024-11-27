package com.eighttoten.service.board;

import com.eighttoten.domain.member.Member;

public interface BoardHeartService {
    void add(Long boardId, Member member);
    void delete(Long boardId, Member member);
}
