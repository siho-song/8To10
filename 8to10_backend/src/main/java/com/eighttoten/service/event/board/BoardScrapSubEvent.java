package com.eighttoten.service.event.board;

import com.eighttoten.domain.board.Board;

public class BoardScrapSubEvent extends BoardEvent{

    public BoardScrapSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.subScrap();
    }
}
