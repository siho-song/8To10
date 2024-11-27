package com.eighttoten.service.event.board;

import com.eighttoten.domain.board.Board;

public class BoardHeartSubEvent extends BoardEvent{

    public BoardHeartSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.subLike();
    }
}
