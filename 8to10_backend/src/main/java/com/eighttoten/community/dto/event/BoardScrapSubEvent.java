package com.eighttoten.community.dto.event;

import com.eighttoten.community.domain.Board;

public class BoardScrapSubEvent extends BoardEvent{

    public BoardScrapSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.subScrap();
    }
}
