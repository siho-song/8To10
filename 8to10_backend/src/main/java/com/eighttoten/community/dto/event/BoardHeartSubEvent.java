package com.eighttoten.community.dto.event;

import com.eighttoten.community.domain.Board;

public class BoardHeartSubEvent extends BoardEvent{

    public BoardHeartSubEvent(Long boardId) {
        super(boardId);
    }

    @Override
    public void execute(Board board) {
        board.subLike();
    }
}
