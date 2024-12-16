package com.eighttoten.community.dto.event;

import com.eighttoten.community.domain.Board;

public class BoardHeartAddEvent extends BoardEvent {

    public BoardHeartAddEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.addLike();
    }
}
