package com.eighttoten.community.dto.event;

import com.eighttoten.community.domain.Board;

public class BoardScrapAddEvent extends BoardEvent{

    public BoardScrapAddEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.addScrap();
    }
}
