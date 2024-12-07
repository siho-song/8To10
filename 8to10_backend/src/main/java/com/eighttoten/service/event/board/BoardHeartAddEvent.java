package com.eighttoten.service.event.board;

import com.eighttoten.domain.board.Board;

public class BoardHeartAddEvent extends BoardEvent {

    public BoardHeartAddEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.addLike();
    }
}
