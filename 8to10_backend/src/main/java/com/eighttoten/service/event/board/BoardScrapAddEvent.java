package com.eighttoten.service.event.board;

import com.eighttoten.domain.board.Board;

public class BoardScrapAddEvent extends BoardEvent{

    public BoardScrapAddEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.addScrap();
    }
}
